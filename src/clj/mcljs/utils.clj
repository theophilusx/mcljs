;;      Filename: utils.clj
;; Creation Date: Thursday, 15 January 2015 07:14 AM AEDT
;; Last Modified: Friday, 23 January 2015 08:42 AM AEDT
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
  (not (nil? qty)))

(defn valid-price? [price]
  (not (nil? price)))

(defn valid-tax? [tax]
  (not (nil? tax)))

(defn valid-discount? [discount]
  (not (nil? discount)))

(defn valid-total? [total]
  (if (re-matches #"^\$\d+\.\d\d" total)
    true
    false))
