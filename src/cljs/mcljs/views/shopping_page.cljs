;;      Filename: shopping_page.cljs
;; Creation Date: Monday, 22 December 2014 09:28 AM AEDT
;; Last Modified: Wednesday, 24 December 2014 06:36 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.views.shopping-page
  (:require [json-html.core :refer [edn->hiccup]]
            [reagent.core :refer [atom]]
            [reagent.session :refer [get put! assoc-in!]]
            [reagent-forms.core :refer [bind-fields]]
            [reagent.validation :as val]
            [reagent.format :as fmt]
            [mcljs.views.common :refer [row input numeric-input dump-state]]))

(def shopping-template
  [:div
   [:legend "Shopping Calculator"]
   (numeric-input "Quantity" :quantity)
   [:div.alert.alert-danger {:field :alert :id :error-quantity}]
   (numeric-input "Price $" :price)
   [:div.alert.alert-danger {:field :alert :id :error-price}]
   (row "Tax Rate (%)" [:input.form-control {:field :numeric
                                             :id :tax
                                             :min 0
                                             :max 100}])
   [:div.alert.alert-danger {:field :alert :id :error-tax}]
   (row "Discount ($)" [:input.form-control {:field :numeric
                                             :id :discount}])
   [:div.alert.alert-danger {:field :alert :id :error-discount}]
   (row "Total $" [:input.form-control {:field :numeric
                                        :id :total
                                        :disabled true}])])

(defn calc-total [quantity price tax discount]
  (-> (* quantity price)
      (* (+ 1 (/ tax 100)))
      (- discount)
      (.toFixed 2)))

(defn valid-quantity? [order]
  (let [qty (:quantity @order)]
    (if (not (and (> qty 0)
                  (= (rem qty 1) 0)))
      (do
        (swap! order
               #(assoc % :error-quantity
                       "Quantity must be greater than 0 and a whole number!"))
        false)
      (do
        (swap! order #(assoc % :error-quantity nil))
        true))))

(defn valid-price? [order]
  (let [price (:price @order)]
    (if (not (> price 0))
      (do
        (swap! order #(assoc % :error-price
                             "Price must be greater than 0!"))
        false)
      (do
        (swap! order #(assoc % :error-price nil))
        true))))

(defn valid-tax? [order]
  (let [tax (:tax @order)]
    (if (not (and (>= tax 0)
                  (<= tax 100)))
      (do
        (swap! order #(assoc % :error-tax
                             "Tax must be between 0 and 100!"))
        false)
      (do
        (swap! order #(assoc % :error-tax nil))
        true))))

(defn valid-discount? [order]
  (let [discount (:discount @order)
        price (:price @order)
        quantity (:quantity @order)]
    (if (not (< discount (* quantity price)))
      (do
        (swap! order #(assoc % :error-discount
                             "Discount must be less than full price!"))
        false)
      (do
        (swap! order #(assoc % :error-discount nil))
        true))))

(defn valid-order? [order]
  (let [qty-ok (valid-quantity? order)
        price-ok (valid-price? order)
        tax-ok (valid-tax? order)
        discount-ok (valid-discount? order)]
    (if (and qty-ok price-ok tax-ok discount-ok) true false)))

(defn save-order [order]
  (put! :order {:quantity (:quantity @order)
                :price (:price @order)
                :tax (:tax @order)
                :discount (:discount @order)
                :total (:total @order)})
  (clojure.core/reset! order {:quantity 1
                              :price 0.00
                              :tax 0
                              :discount 0
                              :total 0.00}))

(defn shopping-page []
  (let [order (atom {:quantity 1
                     :price 0.00
                     :tax 0
                     :discount 0
                     :total 0.00})]
    (fn []
      [:div
       [bind-fields shopping-template order
        (fn [[id] val {:keys [quantity price tax discount] :as doc}]
          (when (and (some #{id} [:quantity :price :tax :discount])
                     quantity price tax discount)
            (assoc doc :total (calc-total quantity price tax discount))))]
       [:button.btn.btn-default {:type "submit"
                                 :on-click (fn []
                                             (if (valid-order? order)
                                               (save-order order)))} "Submit"]
       [:hr]
       [:h3 "Order"]
       [:div (dump-state [:quantity :price :tax :discount :total] @order)]])))
