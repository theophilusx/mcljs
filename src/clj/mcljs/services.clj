;;      Filename: services.clj
;; Creation Date: Monday, 12 January 2015 03:46 PM AEDT
;; Last Modified: Sunday, 15 February 2015 03:23 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.services
  (:require [cheshire.core :refer [generate-string]]
            [clojurewerkz.money.amounts :as ma]
            [liberator.core :refer [defresource]]
            [mcljs.utils :as u]))

(defonce orders (atom {}))

;; value validation same for both clj and cljs, so we really
;; should just have a single source which can be used for both
;; Sound like a job for cljx!
(defn is-malformed?
  "Tests to see if the request was properly formed"
  [{:keys [quantity price tax discount]}]
  (or (not (u/valid-quantity? quantity))
      (not (u/valid-price? price))
      (not (u/valid-tax? tax))
      (not (u/valid-discount? discount))))

(defn get-malformed-msg
  "Generate a message which identifies why request was malformed"
  [{:keys [quantity price tax discount] :as params}]
  (if (empty? params)
    "Error: Form parameters not supplied"
    (str "Error: Missing parameters "
         (if (not (u/valid-quantity? quantity))
           " Quantity must exist and be a whole number.")
         (if (not (u/valid-price? price))
           " Price must exist and be in a valid currency form.")
         (if (not (u/valid-tax? tax))
           " Tax must exist and be a whole number between 0 and 100.")
         (if (not (u/valid-discount? discount))
           " Discount must exist and be a valid currency form."))))

(defn calc
  "Perform the basic shopping total calculation"
  [params]
  (let [q (bigdec (:quantity  params))
        p (ma/parse (str "AUD " (:price params)))
        t (bigdec (:tax params))
        tax (with-precision 2 (inc (/ t 100)))
        d (ma/parse (str "AUD " (:discount params)))]
    (-> (ma/multiply p q)
        (ma/multiply tax)
        (ma/minus d))))

(defn calculate-total
  "Calculate the shopping total and wrap it in a json object"
  [params]
  {:total (calc params)})

(defn handle-malformed-calc
  "Handler for malformed shopping calculation request"
  [params]
  {:message (get-malformed-msg params)})

(defn handle-malformed-order
  "Handle a malformed order request"
  [{:keys [total] :as params}]
  (let [msg (get-malformed-msg params)]
    (if-not (u/valid-total? total)
      {:message (str msg " Total must be more than $0.00")}
      {:message msg})))

(defn handle-order
  "Handle an order request"
  [{:keys [quantity price tax discount total]}]
  (let [order-id (str (java.util.UUID/randomUUID))]
    (swap! orders #(assoc % order-id {:order-id order-id
                                      :quantity quantity
                                      :price price
                                      :tax tax
                                      :discount discount
                                      :total total}))
    {:order-id order-id}))

(defresource shopping-calc
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :malformed? (fn [context]
                (let [params (get-in context [:request :params])]
                  (or (empty? params)
                      (is-malformed? params))))
  :handle-malformed (fn [context]
                      (let [params (get-in context [:request :params])]
                        (generate-string (handle-malformed-calc params))))
  :handle-created (fn [context]
                    (let [params (get-in context [:request :params])]
                      (generate-string (calculate-total params)))))

(defresource place-order
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :malformed? (fn [context]
                (let [params (get-in context [:request :params])]
                  (or (empty? params)
                      (is-malformed? params)
                      (not (u/valid-total? (:total params))))))
  :handle-malformed (fn [context]
                      (let [params (get-in context [:request :params])]
                        (generate-string (handle-malformed-order params))))
  :handle-created (fn [context]
                    (let [params (get-in context [:request :params])]
                      (generate-string (handle-order params)))))

(defresource list-orders
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (generate-string @orders)))
