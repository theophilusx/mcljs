;;      Filename: login_page.cljs
;; Creation Date: Friday, 05 December 2014 05:58 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:29 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.views.login-page
  (:require [json-html.core :refer [edn->hiccup]]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [reagent.validation :as val]
            [reagent.session :as session :refer [get put! assoc-in!]]))

(defn row [label input]
  [:div.form-group
   [:div.row
    [:div.col-md-2 [:label label]]
    [:div.col-md-5 input]]])

(defn input [label type id]
  (row label [:input.form-control {:field type :id id}]))

(def login-template
  [:div
   [:legend "Login Form"]
   (input "E-Mail Address" :email :email)
   [:div.alert.alert-danger {:field :alert :id :errors.email}]
   (input "Password" :password :password)
   [:div.alert.alert-danger {:field :alert :id :errors.password}]])

(defn valid-login? [person]
  (.log js/console (str @person))
  (let [email-ok (val/is-email? (:email @person))
        pass-ok (val/min-length? (:password @person) 8)]
    (if (not email-ok)
      (swap! person #(assoc-in % [:errors :email]
                               "Not a valid e-mail address!"))
      (swap! person #(assoc-in % [:errors :email] nil)))
    (if (not pass-ok)
      (swap! person
             #(assoc-in %
                        [:errors :password]
                        "Password must be longer than 8 characters!"))
      (swap! person #(assoc-in % [:errors :password] nil)))
    (if (and email-ok pass-ok) true false)))

(defn save-state [person]
  (let [people (get :people {})]
    (put! :people (assoc people (:email @person)
                         {:email (:email @person)
                          :password (:password @person)}))
    (reset! person {})))

(defn login-page []
  (let [person (atom {})]
    (fn []
      [:div
       [bind-fields login-template person]
       [:button.btn.btn-default
        {:type "submit"
         :on-click (fn []
                     (if (valid-login? person)
                       (save-state person)))} "Submit"]
       [:hr]
       [:h3 "Person"]
       [:p (str @person)]])))
