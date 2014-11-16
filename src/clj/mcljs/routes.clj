;      Filename: routes.clj
; Creation Date: Thursday, 13 November 2014 06:50 PM AEDT
; Last Modified: Sunday, 16 November 2014 09:52 AM AEDT>
;   Description:
;
(ns mcljs.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.handler.dump :refer [handle-dump]]
            [environ.core :refer [env]]
            [mcljs.layout :as layout]
            [cemerick.austin.repls :refer (browser-connected-repl-js)]))

(defn index-page []
  (layout/render "app.html" {:brepl (if (env :dev)
                                      (browser-connected-repl-js)
                                      nil)}))

(defroutes app-routes
  (GET "/" [] "Hello to Clojure World")
  (GET "/index" [] (index-page))
  (ANY "/request" [] handle-dump)
  (route/resources "/")
  (route/not-found "Not Found"))
