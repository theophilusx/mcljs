;;      Filename: project.clj
;; Creation Date: Thursday, 13 November 2014 03:18 PM AEDT
;; Last Modified: Wednesday, 14 January 2015 06:18 PM AEDT>
;;   Description:
;;

(defproject mcljs "0.1.0-SNAPSHOT"
  :description "A basic project skeleton using reagent, austin and selmar"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs" "src/brepl"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [acyclic/squiggly-clojure "0.1.2-SNAPSHOT"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [compojure "1.3.1"]
                 [environ "1.0.0"]
                 [selmer "0.7.9"]
                 [prone "0.8.0"]
                 [json-html "0.2.8"]
                 [reagent "0.4.3"]
                 [reagent-utils "0.1.2"]
                 [reagent-forms "0.3.9"]
                 [secretary "1.2.1"]
                 [liberator "0.12.2"]
                 [cheshire "5.4.0"]
                 [com.cemerick/piggieback "0.1.4"]
                 [clojurewerkz/money "1.6.0"]]

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [com.cemerick/austin "0.1.6"]
            [lein-cljsbuild "1.0.4"]]

  :ring {:handler mcljs.handler/app
         :init mcljs.handler/init
         :destroy mcljs.handler/destroy}

  :main ^:skip-aot mcljs.core

  :target-path "target/%s"

  :repl-options {:init-ns mcljs.repl}

  :cljsbuild {:builds
              {:dev {:source-paths ["src/cljs" "src/brepl"]
                     :compiler {:output-to "resources/public/js/app.js"
                                :output-dir "resources/public/js/out"
                                :source-map "resources/public/js/app.js.map"
                                :optimizations :whitespace
                                :pretty-print true}}}
              :prod {:source-paths ["src/cljs"]
                     :compiler {:output-to "resources/public/js/app.js"
                                :output-dir "resources/public/js/out"
                                :source-map "resources/public/js/app.js.map"
                                :optimizations :advanced
                                :pretty-print false}}}

  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]
                                  [ring/ring-devel "1.3.2"]]
                   :env {:dev true
                         :squiggly {:checkers [:eastwood]
                                    :eastwood-exclude-linters [:unlimited-use]}}}
             :prod {:ring {:open-browser? false
                           :stacktrace? false
                           :auto-reload? false}
                    :env {:prod true}}})
