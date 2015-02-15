;;      Filename: state.cljs
;; Creation Date: Friday, 23 January 2015 10:54 AM AEDT
;; Last Modified: Friday, 23 January 2015 02:29 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.state
  (:require [reagent.session :as session]))

(defn render-vec [v]
  (into [:ul] (for [i v]
                [:li (str i)])))

(defn render-map [m]
  (let [ks (keys m)]
    (into [:table.table.table-bordered]
          (for [k ks]
            [:tr [:th (str k)] [:td (let [c (get m k)]
                                      (cond
                                        (vector? c) (render-vec c)
                                        (map? c) (render-map c)
                                        :else (str c)))]]))))

(defn render-state []
  (let [smap (dissoc @session/state :current-page :nav)]
    [:div
     (if (not (empty? smap))
       (render-map smap)
       [:p "No additional state values to display"])]))

(defn state-keys []
  (let [ks (keys @session/state)]
    [:div
     [:h4 "Current state keys"]
     (into [:ul] (for [k ks]
                   [:li (str k)]))]))
