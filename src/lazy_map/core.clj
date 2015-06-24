(ns lazy-map.core
  "Create lazy-maps, whose values are only calculated when they are
  looked up for the first time, see [[lazy-map]]"
  {:author "Artur Malabarba"})

(defprotocol Holder
  "Hold a value."
  (getv [a] "Return object, resolving it if LazyValue."))

(extend-protocol Holder
  Object
  (getv [a] a))

;;; LazyVal is a simple wrapper around `delay` that extends [[Holder]].
;; Don't call the constructor directly, use [[lazy-val]] instead.
(deftype LazyVal [delay]
  Holder
  (getv [_] (force delay)))

(defmacro lazy-val
  "Return a LazyVal that runs `body` when accessed.
  Somewhat analogous to `clojure.core/lazy-seq`. See also [[lazy-map]]."
  [& body]
  `(LazyVal. (delay ~@body)))

(defn lazy-val-fn
  "Return a lazy value that evaluates `function` when accessed.
  Designed to offers a non-macro alternative to [[lazy-val]]."
  [function]
  (lazy-val (function)))


;;; Map Definition
(deftype LazyMap [contents]
  clojure.lang.IPersistentMap
  (assoc [_ k v]
    (LazyMap. (.assoc contents k v)))
  (assocEx [_ k v]
    (LazyMap. (.assocEx contents k v)))
  (without [_ k]
    (LazyMap. (.without contents k)))

  java.lang.Iterable
  (iterator [this]
    (.iterator (into {} (map (fn [[k v]] [k (getv v)]) contents))))

  clojure.lang.Associative
  (containsKey [_ k]
    (.containsKey contents k))
  (entryAt [_ k]
    (getv (.entryAt contents k)))

  clojure.lang.IPersistentCollection
  (count [_] (.count contents))
  (empty [_] (.empty contents))
  (cons [_ o]
    (LazyMap. (.cons contents o)))
  (equiv [_ o]
    (and (isa? (class o) LazyMap)
         (.equiv contents (.contents o))))

  clojure.lang.Seqable
  (seq [_] (.seq contents))
  
  clojure.lang.ILookup
  (valAt [_ k]
    (getv (.valAt contents k)))
  (valAt [_ k not-found]
    (getv (.valAt contents k not-found))))


;;; Map creation
(defmacro lazy-map
  "Return a LazyMap created from a map `m`.
  The values in `m` are only evaluated when accessed."
  [m]
  `(LazyMap.
    ~(into {} (map (fn [[k v]] [k `(lazy-val ~v)]) m))))
