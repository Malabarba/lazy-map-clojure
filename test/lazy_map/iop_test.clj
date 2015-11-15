(ns lazy-map.iop-test
  (:require [lazy-map.iop :refer :all]
            [clojure.test :refer :all]))


;;; Utility functions
(deftest test-enum-to-keyword
  (testing "Only enum is allowed"
    (are [x] (try (enum-to-keyword x) (catch Throwable e true))
      10 :a 'and "a" inc)))

;;; Test the actual protocol
(deftest map-to-lazy-map
  (testing "Trivial conversion"
    (let [m {:a 1}]
      (is (= m (to-lazy-map m))))))

(deftest a-test
  (testing "Basic extensions"
    (extend-lazy-map String)
    (extend-lazy-map Exception)
    (let [s (to-lazy-map "The String")
          e (to-lazy-map (Exception. "The Exception"))]
      (is (> (count s) 11))
      (is (>= (count e) 9))
      (is (= (:length s) 10))
      (is (not (:length e)))
      (is (not (:notify-all e))))))

(defn ex [] (Exception. "The Exception"))

(deftest b-test
  (testing "Exclusions post-fns and allow-impure"
    (extend-lazy-map String
                     :exclude [toString]
                     :post-fns {length dec})
    (extend-lazy-map Exception
                     :allow-impure true)
    (let [s (to-lazy-map "The String")
          e (to-lazy-map (ex))]
      (is (> (count s) 10))
      (is (>= (count e) 13))
      (is (= (:length s) 9))
      (is (try (:notify-all e)
               (catch Exception e true))))))

(deftest c-test
  (testing "Catch-exceptions and keyname"
    (extend-lazy-map Exception
                     :allow-impure true
                     :catch-exceptions true
                     :keyname {toString :boogie})
    (is (not (:notify-all (to-lazy-map (ex)))))
    (is (= (:boogie (to-lazy-map (ex))) "java.lang.Exception: The Exception"))))
