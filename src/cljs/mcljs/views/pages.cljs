;;      Filename: pages.cljs
;; Creation Date: Monday, 01 December 2014 06:20 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:33 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.views.pages
  (:require [mcljs.views.home-page :refer [home-page]]
            [mcljs.views.about-page :refer [about-page]]
            [mcljs.views.login-page :refer [login-page]]))

(def pages {:home-page home-page
            :about-page about-page
            :login-page login-page})
