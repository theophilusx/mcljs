;;      Filename: services.clj
;; Creation Date: Monday, 12 January 2015 03:46 PM AEDT
;; Last Modified: Friday, 16 January 2015 01:58 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;
(ns mcljs.services
  (:require [mcljs.utils :as u]
            [liberator.core :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [clojurewerkz.money.amounts :as ma]
            [clojurewerkz.money.currencies :as mc]
            [clojurewerkz.money.json]))

(defn calc [params]
  (let [q (bigdec (:quantity  params))
        p (ma/parse (str "AUD " (:price params)))
        t (bigdec (:tax params))
        tax (with-precision 2 (+ 1 (/ t 100)))
        d (ma/parse (str "AUD " (:discount params)))]
    (-> (ma/multiply p q)
        (ma/multiply tax)
        (ma/minus d))))

(defn calculate-total [params]
  {:total (calc params)})

(defn handle-malformed [params]
  (if (empty? params)
    (str "Error: Form parameters not supplied.")
    (str "Error:" (if (not (u/valid-quantity? (:quantity params)))
                    " Quantity must exist and be a whole number."
                    "")
         (if (not (u/valid-price? (:price params)))
           " Price must exist and be in a valid currency form."
           "")
         (if (not (u/valid-tax? (:tax params)))
           " Tax must exist and be a whole number between 0 and 100."
           "")
         (if (not (u/valid-discount? (:discount params)))
           " Discount must exist and be a valid currency form."
           ""))))

(defresource shopping-calc
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :malformed? (fn [context]
                (let [params (get-in context [:request :params])]
                  (or (empty? params)
                      (not (u/valid-quantity? (:quantity params)))
                      (not (u/valid-price? (:price params)))
                      (not (u/valid-tax? (:tax params)))
                      (not (u/valid-discount? (:discount params))))))
  :handle-malformed (fn [context]
                      (let [params (get-in context [:request :params])]
                        (handle-malformed params)))
  :handle-created (fn [context]
                    (let [params (get-in context [:request :params])]
                      (generate-string (calculate-total params)))))

(defresource place-order
  :allowed-methods [:post]
  :available-media-types ["application/json"]
  :handle-created (fn [context]
                    (generate-string {:order-status "Order has been placed"})))
