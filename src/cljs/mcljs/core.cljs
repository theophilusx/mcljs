;      Filename: core.cljs
; Creation Date: Saturday, 15 November 2014 10:19 AM AEDT
; Last Modified: Sunday, 21 December 2014 05:47 PM AEDT>
;   Description:
;

(ns mcljs.core
  (:require [reagent.core :as reagent]
            [reagent.session :as session :refer [get]]
;;            [mcljs.session :as session :refer [global-state]]
            [mcljs.routes :as routes]
            [mcljs.views.common :as common]))

(defn dump-state []
  (str "Errors: " (get :errors "No Errors") "\n"
       "People: " (get :people "No records")))

(defn page-render []
  [:div.container
   [common/header]
   [(get :current-page)]
   [:h3 "State"]
   [:label (dump-state)]])

(defn page-component []
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [common/nav-bar]
                          (.getElementById js/document "navbar"))

(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
