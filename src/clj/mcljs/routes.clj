;;      Filename: routes.clj
;; Creation Date: Thursday, 13 November 2014 06:50 PM AEDT
;; Last Modified: Thursday, 22 January 2015 06:07 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.handler.dump :refer [handle-dump]]
            [selmer.parser :refer [render-file]]
            [environ.core :refer [env]]
            [mcljs.services :refer [shopping-calc place-order]]
            [mcljs.debug :refer [debug-request]]
            [cemerick.austin.repls :refer (browser-connected-repl-js)]))

(defn index-page []
  (render-file "templates/app.html" {:brepl (if (env :dev)
                                              (browser-connected-repl-js)
                                              nil)
                                     :dev (env :dev)}))

(defroutes api-routes
  (ANY "/api/debug" [] debug-request)
  (ANY "/api/order/calc" [] shopping-calc)
  (ANY "/api/order/place" [] place-order))

(defroutes app-routes
  (GET "/" [] (index-page))
  (ANY "/request" [] handle-dump)
  (route/resources "/")
  (route/not-found "Not Found"))
