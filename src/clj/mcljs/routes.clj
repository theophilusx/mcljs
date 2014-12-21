;      Filename: routes.clj
; Creation Date: Thursday, 13 November 2014 06:50 PM AEDT
; Last Modified: Monday, 01 December 2014 07:00 PM AEDT>
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
  (GET "/" [] (index-page))
  (ANY "/request" [] handle-dump)
  (route/resources "/")
  (route/not-found "Not Found"))
