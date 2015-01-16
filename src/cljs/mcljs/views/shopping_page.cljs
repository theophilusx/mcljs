;;      Filename: shopping_page.cljs
;; Creation Date: Monday, 22 December 2014 09:28 AM AEDT
;; Last Modified: Friday, 16 January 2015 02:05 PM AEDT
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
            [mcljs.views.common :refer [row input numeric-input dump-state]]
            [ajax.core :refer [GET POST]]))

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
   (row "Total $" [:input.form-control {:field :text
                                        :id :total
                                        :disabled true}])
   [:div.alert.alert-success {:field :alert :id :order-status}]])

(defn handle-calc [order]
  (fn [response]
    (let [rsp (js->clj response :keywordize-keys true)
          rkeys (keys rsp)]
      (swap! order #(assoc % :total (:total rsp))))))

(defn handle-order [order]
  (fn [response]
    (let [rsp (js->clj response :keywordize-keys true)]
      (swap! order #(assoc % :order-status (:order-status rsp)
                           :quantity 1
                           :price 0.00
                           :tax 0
                           :discount 0
                           :total "?")))))

(defn handle-ajax-error [order]
  (fn [err]
    (let [rsp (:response err)]
      (.log js/console (str "Error: " (:status err) " " (:status-text err)))
      (.log js/console (str err))
      (.log js/console (str "Response:"))
      (.log js/console (str rsp))
      (swap! order #(assoc % :error-total (str (:status err) " "
                                               (:status-text err)))))))

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

(defn post-calculate [order]
  (POST "/calctotal" {:params {:quantity (:quantity @order)
                               :price (:price @order)
                               :tax (:tax @order)
                               :discount (:discount @order)}
                      :format :json
                      :response-format :json
                      :keywords? true
                      :handler (handle-calc order)
                      :error-handler (handle-ajax-error order)}))

(defn post-order [order]
  (POST "/placeorder" {:params {:quantity (:quantity @order)
                                :price (:price @order)
                                :tax (:tax @order)
                                :discount (:discount @order)
                                :total (:total @order)}
                       :format :json
                       :response-format :json
                       :keywords? true
                       :handler (handle-order order)
                       :error-handler (handle-ajax-error order)}))

(defn shopping-page []
  (let [order (atom {:quantity 1
                     :price 0.00
                     :tax 0
                     :discount 0
                     :total "?"})]
    (fn []
      [:div
       [bind-fields shopping-template order]
       [:button.btn.btn-default {:type "submit"
                                 :on-click (fn []
                                             (if (valid-order? order)
                                               (post-calculate order)))}
        "Calculate Total"]
       [:button.btn.btn-default {:type "submit"
                                 :on-click (fn []
                                             (if (valid-order? order)
                                               (post-order order)))}
        "Place Order"]
       [:hr]
       [:h3 "Order"]
       [:div (dump-state [:quantity :price :tax :discount :total] @order)]])))
