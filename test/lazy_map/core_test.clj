(ns lazy-map.core-test
  (:require [clojure.test :refer :all]
            [lazy-map.core :refer :all]))

(deftest lazy
  (let [a (atom 0)
        m (lazy-map {:a (swap! a inc)})]
    (is (= 0 @a))
    (is (= 1 (:a m)))
    (is (= 1 @a))
    (is (= 1 (:a m)))
    (is (= 1 @a))))

(deftest safe
  (let [sym (gensym "!")
        m (lazy-map {:a nil :b "ok" :c 1 :d true
                     :e sym})]
    (are [k v] (= (k m) v)
      :a nil :b "ok" :c 1 :d true :e sym))
  (let [sym (gensym "!")
        m (lazy-map.core.LazyMap {:a nil :b "ok" :c 1 :d true
                                  :e sym})]
    (are [k v] (= (k m) v)
      :a nil :b "ok" :c 1 :d true :e sym)))
