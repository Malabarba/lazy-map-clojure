(ns lazy-map.test-runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [lazy-map.core-test]))

(doo-tests 'lazy-map.core-test)