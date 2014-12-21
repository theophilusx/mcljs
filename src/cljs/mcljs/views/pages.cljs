;      Filename: pages.cljs
; Creation Date: Monday, 01 December 2014 06:20 PM AEDT
; Last Modified: Friday, 05 December 2014 06:14 PM AEDT>
;   Description:
;

(ns mcljs.views.pages
  (:require [mcljs.views.home-page :refer [home-page]]
            [mcljs.views.about-page :refer [about-page]]
            [mcljs.views.login-page :refer [login-page]]))

(def pages {:home-page home-page
            :about-page about-page
            :login-page login-page})
