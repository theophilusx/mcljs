;      Filename: common.cljs
; Creation Date: Monday, 01 December 2014 06:17 PM AEDT
; Last Modified: Sunday, 21 December 2014 04:57 PM AEDT>
;   Description:
;
(ns mcljs.views.common
  (:require  [reagent.session :as session :refer [get]]))

(defn active? [state val]
  (if (= state val) "active" ""))

(defn header []
  [:div.row
   [:p (str "This is the " (get :env) " environment")]])

(defn nav-bar []
  [:ul {:class "nav navbar-nav"}
   [:li {:class (active? (get :nav) "home")}
    [:a {:href "#/"} "Home"]]
   [:li {:class (active? (get :nav) "about")}
    [:a {:href "#/about"} "About"]]
   [:li {:class (active? (get :nav) "login")}
    [:a {:href "#/login"} "Login"]]])
