;;      Filename: core.cljs
;; Creation Date: Saturday, 15 November 2014 10:19 AM AEDT
;; Last Modified: Friday, 23 January 2015 12:23 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.core
  (:require [reagent.core :as reagent]
            [reagent.session :as session :refer [get state]]
            [mcljs.routes :as routes]
            [mcljs.views.common :as common]
            [mcljs.state :refer [render-state state-keys]]))

(defn page-render []
  [:div
   [(get :current-page)]
   [:div.container
    [:div.row
     [:div
      [:h3 "State"]
      (state-keys)
      (render-state)]]]])

(defn page-component []
  (reagent/create-class {:component-will-mount routes/app-routes
                         :render page-render}))

;; initialize app
(reagent/render-component [common/nav-bar]
                          (.getElementById js/document "navbar"))

(reagent/render-component [page-component]
                          (.getElementById js/document "app"))
