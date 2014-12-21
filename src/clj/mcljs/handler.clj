;;      Filename: handler.clj
;; Creation Date: Thursday, 13 November 2014 03:15 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:37 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.handler
  (:require [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [mcljs.routes :refer [app-routes]]))

(def app
  (if (env :dev)
    (-> #'app-routes
        (wrap-reload)
        (wrap-defaults site-defaults)
        (wrap-error-page)
        (wrap-exceptions))
    (-> #'app-routes
        (wrap-defaults site-defaults)
        (wrap-error-page))))
