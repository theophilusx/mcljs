;;      Filename: handler.clj
;; Creation Date: Thursday, 13 November 2014 03:15 PM AEDT
;; Last Modified: Sunday, 15 February 2015 02:51 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.handler
  (:require [compojure.core :refer :all]
            [environ.core :refer [env]]
            [liberator.dev :refer [wrap-trace]]
            [mcljs.routes :refer [api-routes app-routes]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [api-defaults
                                              wrap-defaults]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [selmer.middleware :refer [wrap-error-page]]))

;; Really need to fix this up so that the right middleware only gets
;; applied to the routes which need it. Probably need to use a
;; compojure context
;; There is an issue with wrap-defaults which needs to be fixed before
;; wrap-defaults can be used with wrap-routes
(def app
  (if (env :dev)
    (routes (-> api-routes
                (wrap-reload)
                (wrap-routes wrap-json-params)
                (wrap-routes wrap-defaults api-defaults)
                (wrap-routes wrap-trace :header :ui))
            (-> app-routes
                (wrap-routes wrap-error-page)
                (wrap-routes wrap-exceptions)))
    (routes (-> api-routes
                (wrap-routes wrap-json-params)
                (wrap-routes wrap-defaults api-defaults))
            app-routes)))
