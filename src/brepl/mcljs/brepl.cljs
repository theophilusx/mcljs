;      Filename: brepl.cljs
; Creation Date: Sunday, 16 November 2014 10:51 AM AEDT
; Last Modified: Sunday, 16 November 2014 11:14 AM AEDT>
;   Description:
;
(ns mcljs.brepl
  (:require [clojure.browser.repl]))

(defn hello
  []
  (js/alert "hello"))

(defn whoami
  []
  (.-userAgent js/navigator))

(enable-console-print!)
