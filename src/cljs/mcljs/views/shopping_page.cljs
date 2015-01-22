;;      Filename: shopping_page.cljs
;; Creation Date: Monday, 22 December 2014 09:28 AM AEDT
;; Last Modified: Friday, 23 January 2015 08:47 AM AEDT
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

(def error-msg {:quantity "Quantity must be > 0 and a whole number"
                :price "Price must be > 0 in whole dollars or dollars and cents"
                :tax "Tax must be a whole number between 0 and 100"
                :discount (str "Discount must be less than total price i.e. "
                               "price * quantity and in whole dollars or "
                               "dollars plus cents")
                :order (str "All fields must be completed with valid "
                            "input before the total can be calculated")
                :place-order (str "You must fill in all fields and calculate "
                                  "the order total before you can place "
                                  "an order")})

(defn set-error [tag doc]
  (assoc-in doc [:error tag] (tag error-msg)))

(defn clear-error [tag doc]
  (assoc-in doc [:error tag] nil))

(def shopping-template
  [:div
   [:legend "Shopping Calculator"]
   (numeric-input "Quantity" :quantity)
   [:div.alert.alert-danger {:field :alert :id :error.quantity}]
   (numeric-input "Price $" :price)
   [:div.alert.alert-danger {:field :alert :id :error.price}]
   (row "Tax Rate (%)" [:input.form-control {:field :numeric
                                             :id :tax
                                             :min 0
                                             :max 100}])
   [:div.alert.alert-danger {:field :alert :id :error.tax}]
   (row "Discount ($)" [:input.form-control {:field :numeric
                                             :id :discount}])
   [:div.alert.alert-danger {:field :alert :id :error.discount}]
   (row "Total $" [:input.form-control {:field :text
                                        :id :total
                                        :disabled true}])
   [:div.alert.alert-danger {:field :alert :id :error.total}]
   [:div.alert.alert-success {:field :alert :id :order.status}]])


(defn valid-quantity? [qty]
  (and (> qty 0)
       (= (rem qty 1) 0)))

(defn valid-price? [price]
  (> price 0))

(defn valid-tax? [tax]
  (and (>= tax 0)
       (<= tax 100)))

(defn valid-discount? [discount price quantity]
  (and (>= discount 0)
       (< discount (* quantity price))))

(defn valid-order? [{:keys [quantity price tax discount] :as order}]
  (and (valid-quantity? quantity)
       (valid-price? price)
       (valid-tax? tax)
       (valid-discount? discount price quantity)))

(defn valid-total? [{:keys [total]}]
  (not (= "?" total)))

(defn handle-calc [order]
  (fn [response]
    (let [rsp (js->clj response :keywordize-keys true)
          rkeys (keys rsp)]
      (swap! order #(assoc % :total (:total rsp))))))

(defn handle-order [order]
  (fn [response]
    (let [rsp (js->clj response :keywordize-keys true)]
      (swap! order #(assoc-in % [:order :status] (str "Order successful: "
                                                      "Order ID = "
                                                      (:order-id rsp))))
      (swap! order #(assoc-in % [:error :total] nil))
      (swap! order #(assoc % :quantity 1
                           :price 0.00
                           :tax 0
                           :discount 0
                           :total "?")))))

(defn handle-ajax-error [order]
  (fn [err]
    (let [rsp (js->clj (:response err) :keywordize-keys true)]
      (.log js/console (str "Error: " (:status err) " " (:status-text err)))
      (.log js/console (str err))
      (swap! order #(assoc-in % [:error :total] (str (:status err) " "
                                                     (:message rsp)))))))

(defn post-calculate [order]
  (POST "/api/order/calc" {:params {:quantity (:quantity @order)
                               :price (:price @order)
                               :tax (:tax @order)
                               :discount (:discount @order)}
                           :format :json
                           :response-format :json
                           :keywords? true
                           :handler (handle-calc order)
                           :error-handler (handle-ajax-error order)}))

(defn post-order [order]
  (POST "/api/order/place" {:params {:quantity (:quantity @order)
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
       [bind-fields shopping-template order
        (fn [[id] val {:keys [quantity price tax discount] :as doc}]
          (condp = id
            :quantity (if (not (valid-quantity? val))
                        (set-error :quantity doc)
                        (clear-error :quantity doc))
            :price (if (not (valid-price? val))
                     (set-error :price doc)
                     (clear-error :price doc))
            :tax (if (not (valid-tax? val))
                   (set-error :tax doc)
                   (clear-error :tax doc))
            :discount (if (not (valid-discount? discount price quantity))
                        (set-error :discount doc)
                        (clear-error :discount doc))
            (assoc-in doc [:error :total] (str "Unexpected ID: " id))))
        (fn [id val doc]
          (if (get-in doc [:order :status])
            (assoc-in doc [:order :status] nil)))]
       [:button.btn.btn-default {:type "submit"
                                 :on-click
                                 (fn []
                                   (if (valid-order? @order)
                                     (do
                                       (swap! order
                                              #(assoc-in % [:error :total] nil))
                                       (post-calculate order))
                                     (swap! order
                                            #(assoc-in
                                              % [:error :total]
                                              (:order error-msg)))))}
        "Calculate Total"]
       [:button.btn.btn-default {:type "submit"
                                 :on-click
                                 (fn []
                                   (if (and (valid-order? @order)
                                            (valid-total? @order))
                                     (post-order order)
                                     (swap! order
                                            #(assoc-in
                                              % [:error :total]
                                              (:place-order error-msg)))))}
        "Place Order"]
       [:hr]
       [:h3 "Order"]
       [:div (edn->hiccup @order)]])))
