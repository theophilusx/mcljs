;;      Filename: utils.clj
;; Creation Date: Thursday, 15 January 2015 07:14 AM AEDT
;; Last Modified: Thursday, 15 January 2015 07:32 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;


(ns mcljs.utils)


(defn parse-int [str]
  (try
    (Integer/parseInt str)
    (catch Exception e
      nil)))

(defn valid-int? [str]
  (if (parse-int str) true false))

(defn parse-double [str]
  (try
    (Double/parseDouble str)
    (catch Exception e
      nil)))

(defn valid-double? [str]
  (if (parse-double str) true false))

(defn valid-quantity? [qty]
  (and (not (empty? qty))
       (valid-int? qty)))

(defn valid-price? [price]
  (and (not (empty? price))
       (valid-double? price)))

(defn valid-tax? [tax]
  (and (not (empty? tax))
       (valid-int? tax)))

(defn valid-discount? [discount]
  (and (not (empty? discount))
       (valid-double? discount)))
