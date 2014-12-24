;;      Filename: common.cljs
;; Creation Date: Monday, 01 December 2014 06:17 PM AEDT
;; Last Modified: Wednesday, 24 December 2014 07:11 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.views.common
  (:require  [reagent.session :as session :refer [get]]))

(defn dump-state [ks state-map]
  (into [:table.table.table-bordered]
        (for [k ks]
          [:tr [:th (str k)] [:td (str (k state-map))]])))

(defn row [label input]
  [:div.form-group
   [:div.row
    [:div.col-md-2 [:label label]]
    [:div.col-md-5 input]]])

(defn input [label type id]
  (row label [:input.form-control {:field type :id id}]))

(defn numeric-input [label id]
  (row label [:input.form-control {:field :numeric
                                   :id id}]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.row
   [:p (str "This is the " (get :env) " environment")]])

(defn nav-bar []
  [:ul.nav.navbar-nav
   [:li {:class (active? (get :nav) "home")}
    [:a {:href "#/"} "Home"]]
   [:li {:class (active? (get :nav) "about")}
    [:a {:href "#/about"} "About"]]
   [:li {:class (active? (get :nav) "login")}
    [:a {:href "#/login"} "Login"]]
   [:li {:class (active? (get :nav) "shopping")}
    [:a {:href "#/shopping"} "Shopping"]]])
