;;      Filename: handler.clj
;; Creation Date: Thursday, 13 November 2014 03:15 PM AEDT
;; Last Modified: Wednesday, 21 January 2015 05:22 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.handler
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults
                                              api-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-params]]
            [compojure.core :refer :all]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [mcljs.routes :refer [app-routes api-routes]]
            [liberator.dev :refer [wrap-trace]]))

(def app
  (if (env :dev)
    (routes (-> api-routes
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
