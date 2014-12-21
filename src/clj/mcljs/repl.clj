;;      Filename: repl.clj
;; Creation Date: Saturday, 15 November 2014 06:05 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:38 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.repl
  (:require [cemerick.austin.repls]
            [mcljs.handler :as handlers]
            [ring.adapter.jetty :as jetty]))

(defonce server (atom nil))

(defonce repl-env (reset! cemerick.austin.repls/browser-repl-env
                          (cemerick.austin/repl-env)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (jetty/run-jetty #'handlers/app {:port port
                                             :join? false}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn cljs!
  "Once you have started your Clojure REPL, enter the user namespace and run
   this function."
  []
  (cemerick.austin.repls/cljs-repl repl-env))
