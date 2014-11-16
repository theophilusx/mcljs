;      Filename: core.cljs
; Creation Date: Saturday, 15 November 2014 10:19 AM AEDT
; Last Modified: Saturday, 15 November 2014 04:04 PM AEDT>
;   Description:
;

(ns mcljs.core
  (:require [reagent.core :as reagent :refer [atom]]))

(defn simple-component []
  [:div
   [:p "I am a component!"]
   [:p "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]
   [:p "Hello from clojureScript!"]])

(defn ^:export run []
  (reagent/render-component [simple-component]
                            (.getElementById js/document "app")))
