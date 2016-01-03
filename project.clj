(defproject malabarba/lazy-map "1.1"
  :description "Create lazy maps from data or from java objects. Entry
points are the `core/lazy-map` and the `iop/extend-lazy-map` macros."
  :url "https://github.com/Malabarba/lazy-map-clojure"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[codox "0.8.12"]]
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :scm {:name "git"
        :url  "https://github.com/Malabarba/lazy-map-clojure"}
  :codox {:defaults                  {:doc/format :markdown}
          :src-dir-uri               "https://github.com/Malabarba/lazy-map-clojure/blob/master/"
          :src-linenum-anchor-prefix "L"}

  :profiles {:test      {:test-paths  ["test/cljc"]
                         :global-vars {*warn-on-reflection* true}}
             :test-cljs {:dependencies [[org.clojure/clojurescript "1.7.170"]]
                         :plugins      [[lein-cljsbuild   "1.1.1"]
                                        [lein-doo "0.1.6-rc.1"]]}
             :coveralls {:plugins [[lein-cloverage "1.0.2"]
                                   [lein-shell "0.4.0"]]
                         :aliases {"coveralls" ["do" "cloverage" "--coveralls,"
                                                "shell" "curl" "-F"
                                                "json_file=@target/coverage/coveralls.json"
                                                "https://coveralls.io/api/v1/jobs"]}}
             :cljfmt    {:plugins [[lein-cljfmt "0.3.0"]]
                         :cljfmt  {:indents {as-> [[:inner 0]]}}}
             :eastwood  {:plugins  [[jonase/eastwood "0.2.1"]]
                         :eastwood {:config-files ["eastwood.clj"]}}}
  :cljsbuild
  {:builds
   [{:id           "main"
     :source-paths ["src"]
     ;; :notify-command ["terminal-notifier" "-title" "cljsbuild" "-message"]
     :compiler     {:output-to     "resources/public/js/testable.js"
                    :main          lazy-map.test-runner
                    :optimizations :advanced}}
    {:id           "test"
     :source-paths ["src" "test"]
     :compiler     {:output-to     "resources/public/js/testable.js"
                    :main          lazy-map.test-runner
                    :optimizations :none}}]}
  :aliases {"clj-test"  ["with-profile" "+test" "test"]
            "cljs-test" ["with-profile" "+test-cljs" "do"
                         ["clean"]
                         ["doo" "phantom" "test" "auto"]]})
