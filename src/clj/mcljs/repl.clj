;;      Filename: repl.clj
;; Creation Date: Saturday, 15 November 2014 06:05 PM AEDT
;; Last Modified: Sunday, 15 February 2015 02:52 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.repl
  (:require [mcljs.handler :refer [app]]
            [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defonce repl-env (reset! cemerick.austin.repls/browser-repl-env
                          (cemerick.austin/repl-env)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (jetty/run-jetty #'app {:port port
                                    :join? false
                                    :ssl? true
                                    :ssl-port 3080
                                    :keystore "developer.jks"
                                    :key-password "developer"}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server
  "Stop the jetty server"
  []
  (.stop @server)
  (reset! server nil))

(defn cljs!
  "Once you have started your Clojure REPL, enter the user namespace and run
   this function."
  []
  (cemerick.austin.repls/cljs-repl repl-env))
