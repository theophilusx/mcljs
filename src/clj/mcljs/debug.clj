;;      Filename: debug.clj
;; Creation Date: Thursday, 15 January 2015 01:20 PM AEDT
;; Last Modified: Friday, 16 January 2015 02:09 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.debug
  (:require [mcljs.utils :as u]
            [liberator.core :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]))

(defn context-to-json [ctx]
  (println (str "JSONP: " (get-in ctx [:request :json-params])))
  (println (str "PARAMS: " (get-in ctx [:request :params])))
  (generate-string {:status (:status ctx)
                    :message (:message ctx)
                    :json-params (get-in ctx [:request :json-params])
                    :form-params (get-in ctx [:request :form-params])
                    :params (get-in ctx [:request :params])
                    :request-keys (keys (:request ctx))}))

(defresource debug-request
  :allowed-methods [:get :post]
  :available-media-types ["application/json"]
  :handle-ok (fn [ctx]
               (context-to-json ctx))
  :handle-created (fn [ctx]
                    (context-to-json ctx))
  :handle--options (fn [ctx]
                     (context-to-json ctx))
  :handle-accepted (fn [ctx]
                     (context-to-json ctx))
  :handle-no-content (fn [ctx]
                       (context-to-json ctx))
  :handle-malformed (fn [ctx]
                      (context-to-json ctx))
  :handle-not-acceptable (fn [ctx]
                           (context-to-json ctx))
  :handle-not-found (fn [ctx]
                      (context-to-json ctx))
  :handle-method-not-allowed (fn [ctx]
                               (context-to-json ctx))
  :handle-exception (fn [ctx]
                      (context-to-json ctx)))
