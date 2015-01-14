;;      Filename: handler.clj
;; Creation Date: Thursday, 13 November 2014 03:15 PM AEDT
;; Last Modified: Tuesday, 13 January 2015 04:38 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.handler
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults
                                              api-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
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
;        (wrap-error-page)
 ;       (wrap-exceptions)
        (wrap-trace :header :ui))
    (-> #'app-routes
        (wrap-defaults site-defaults)
        (wrap-error-page))))
