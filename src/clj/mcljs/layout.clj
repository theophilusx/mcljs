;;      Filename: layout.clj
;; Creation Date: Thursday, 13 November 2014 03:45 PM AEDT
;; Last Modified: Monday, 22 December 2014 09:38 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [compojure.response :refer [Renderable]]
            [environ.core :refer [env]]))

(def template-path "templates/")

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type (->> (assoc params
                         :dev
                         (env :dev)
                         :servlet-contexttext
                         (if-let [context (:servlet-context request)]
                           (try
                             (.getContextPath context)
                             (catch IllegalArgumentException _ context))))
                       (parser/render-file (str template-path template))
                       response)
                  "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))
