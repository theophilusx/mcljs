;;      Filename: utils.clj
;; Creation Date: Thursday, 15 January 2015 07:14 AM AEDT
;; Last Modified: Sunday, 15 February 2015 02:45 PM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.utils)


(defn parse-int
  "Convert a string representing an integer to an integer"
  [s]
  (try
    (Integer/parseInt s)
    (catch Exception e
      nil)))

(defn valid-int?
  "Tests a string to verify it represents a valid integer"
  [s]
  (if (parse-int s) true false))

(defn parse-double
  "Parse a string representing a double into a double"
  [s]
  (try
    (Double/parseDouble s)
    (catch Exception e
      nil)))

(defn valid-double?
  "Test a string representing a double value is a valid double"[s]
  (if (parse-double s) true false))

(defn valid-quantity?
  "Test that a quantity value is not nil"
  [qty]
  (not (nil? qty)))

(defn valid-price?
  "Check a price is valid"
  [price]
  (not (nil? price)))

(defn valid-tax?
  "Check a tax value is valid"
  [tax]
  (not (nil? tax)))

(defn valid-discount?
  "Check that a discount value is valid"
  [discount]
  (not (nil? discount)))

(defn valid-total?
  "Check that a total value is valid"
  [total]
  (if (re-matches #"^\$\d+\.\d\d" total)
    true
    false))
