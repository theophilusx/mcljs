;;      Filename: handler.clj
;; Creation Date: Thursday, 13 November 2014 03:15 PM AEDT
;; Last Modified: Friday, 16 January 2015 08:08 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.handler
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults
                                              api-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-params]]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [mcljs.routes :refer [app-routes]]
            [liberator.dev :refer [wrap-trace]]))

(def app
  (if (env :dev)
    (-> #'app-routes
        (wrap-reload)
        (wrap-defaults api-defaults)
        (wrap-json-params)
                                        ;        (wrap-error-page)
                                        ;       (wrap-exceptions)
        (wrap-trace :header :ui))
    (-> #'app-routes
        (wrap-defaults site-defaults)
        (wrap-error-page))))
