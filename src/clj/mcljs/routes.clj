;;      Filename: routes.clj
;; Creation Date: Thursday, 13 November 2014 06:50 PM AEDT
;; Last Modified: Sunday, 15 February 2015 02:53 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.routes
  (:require [cemerick.austin.repls :refer [browser-connected-repl-js]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [mcljs.debug :refer [debug-request]]
            [mcljs.services :refer [place-order shopping-calc]]
            [ring.handler.dump :refer [handle-dump]]
            [selmer.parser :refer [render-file]]))

(defn index-page
  "Handler to generate the index.html page"
  []
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
