(ns lazy-map.core-test
  (:require [clojure.test :refer :all]
            [lazy-map.core :refer :all])
  (:import lazy_map.core.LazyMap))

(deftest sanity
  (is (= (class (lazy-map {:a 1}))
         LazyMap)))

(deftest is-lazy
  (testing "Lazy maps are lazy"
    (let [a (atom 0)
          m (lazy-map {:a (swap! a inc)})]
      (is (= 0 @a))
      (is (= 1 (:a m)))
      (is (= 1 @a))
      (is (= 1 (:a m)))
      (is (= 1 @a)))))

(deftest works-just-like-map
  (testing "lazy-map works like maps"
    (let [sym (gensym "!")
          m (lazy-map {:a nil
                       :b "ok"
                       :c 1
                       :d true
                       :e sym})]
      (are [k v] (= (k m) v)
        :a nil
        :b "ok"
        :c 1
        :d true
        :e sym)))
  (testing "LazyMap. works like maps"
    (let [sym (gensym "!")
          m (LazyMap. {:a nil
                       :b "ok"
                       :c 1
                       :d true
                       :e sym})]
      (are [k v] (= (k m) v)
        :a nil
        :b "ok"
        :c 1
        :d true
        :e sym))))

(deftest printing
  (let [a (atom 0)
        m (lazy-map {:a (swap! a inc)})]
    (testing "Printing format"
      (is (re-find #"\{:a #object\[clojure.lang.Delay 0x[0-9a-f]+ \{:status :pending, :val nil\}\]\}"
                   (pr-str m))))
    (testing "Printing doesn't resolve values"
      (is (= @a 0)))
    (:a m)
    (testing "After resolving, print the value."
      (is (re-find #"\{:a #object\[clojure.lang.Delay 0x[0-9a-f]+ \{:status :ready, :val 1\}\]\}"
                   (pr-str m))))))
