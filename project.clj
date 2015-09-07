(defproject org.clojars.malabarba/lazy-map "0.1.4"
  :description "Create lazy maps from data or from java objects. Entry
points are the `core/lazy-map` and the `iop/extend-lazy-map` macros."
  :url "https://github.com/Malabarba/lazy-map-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[codox "0.8.12"]]
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :scm {:name "git"
        :url "https://github.com/Malabarba/lazy-map-clojure"}
  :codox {:defaults {:doc/format :markdown}
          :src-dir-uri "https://github.com/Malabarba/lazy-map-clojure/blob/master/"
          :src-linenum-anchor-prefix "L"})
