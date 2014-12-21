;;      Filename: routes.cljs
;; Creation Date: Monday, 01 December 2014 06:11 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:35 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.routes
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [reagent.session :as session :refer [put!]]
            [mcljs.views.pages :refer [pages]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

;; History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; Routes
(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (put! :current-page (pages :home-page))
    (put! :nav "home"))

  (defroute "/about" []
    (put! :current-page (pages :about-page))
    (put! :nav "about"))

  (defroute "/login" []
    (put! :current-page (pages :login-page))
    (put! :nav "login"))
  (hook-browser-navigation!))
