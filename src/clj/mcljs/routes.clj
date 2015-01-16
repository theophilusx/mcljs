;;      Filename: routes.clj
;; Creation Date: Thursday, 13 November 2014 06:50 PM AEDT
;; Last Modified: Friday, 16 January 2015 02:01 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.handler.dump :refer [handle-dump]]
            [environ.core :refer [env]]
            [mcljs.layout :as layout]
            [mcljs.services :refer [shopping-calc place-order]]
            [mcljs.debug :refer [debug-request]]
            [cemerick.austin.repls :refer (browser-connected-repl-js)]))

(defn index-page []
  (layout/render "app.html" {:brepl (if (env :dev)
                                      (browser-connected-repl-js)
                                      nil)}))

(defroutes app-routes
  (GET "/" [] (index-page))
  (ANY "/request" [] handle-dump)
  (ANY "/calctotal" request shopping-calc)
  (ANY "/placeorder" request place-order)
  (ANY "/debug" request debug-request)
  (route/resources "/")
  (route/not-found "Not Found"))
