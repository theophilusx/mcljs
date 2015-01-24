;;      Filename: about_page.cljs
;; Creation Date: Monday, 01 December 2014 06:23 PM AEDT
;; Last Modified: Sunday, 25 January 2015 08:06 AM AEDT
;;        Author: Tim Cross <theophilusx AT gmail.com>
;;   Description:
;;

(ns mcljs.views.about-page)

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-6
     [:h3 "About Page"]
     [:p
      (str "This project is essentially an experimental workbench "
           "where I get to try out ideas with Clojure and ClojureScript. "
           "It makes extensive use of the Reagent ClojureScript libraries "
           "and implements a very basic single page web application. "
           "Styling for the web pages is done using Bootstrap.")]
     [:p
      (str "As this is an experimental workbench, things may change "
           "considerably as I experiment with different libraries or "
           "techniques. Clear and consistent design is not a primary "
           "objective of this project. Rather, the objective is to keep "
           "a record of ideas and experiments which I may refer back to "
           "when working on other projects.")]
     [:p
      (str "One of the main goals of this work is to identify and refine my "
           "own Clojure and ClojureScript development environment. While this "
           "environment will always be based on Emacs, clojure-mode and cider, "
           "I am still reviewing whether to base the configuration around "
           "Austin or Figwheel or .... A lot is still evolving at this "
           "level and the final result could well be some hybrid which "
           "takes the good bits from various approaches.")]]
    [:div.col-md-6
     [:h3 "Development Environment"]
     [:ul
      [:li "GNU Emacs 24.4 on Linux and OSX"
       [:ul
        [:li (str "Basic emacs configuration based on a fork of Purcell's "
                  "emacs.d GitHub repository and available as ")
         [:a {:href "https://www.github.com/theophilusx/emacs.d"}
          "a github repository"]]
        [:li (str "Packages managed via emacs lisp package archives using "
                  "package.el")]
        [:li "Key packages include"
         [:ul
          [:li "Paredit Mode"]
          [:li "Clojure Mode"]
          [:li "Cider Mode"]
          [:li "Auto-complete mode"]]]
        [:li "I also make extensive use of Org-mode for notes, TODOs etc"]]]
      [:li "Leiningen for managing the project and builds"
       [:ul
        [:li (str "This project did not start with a single standard "
                  "lein template. At the time, there were 3 or 4 different "
                  "templates available for Reagent based projects. This "
                  "project took bits from most of them.")]]]
      [:li "OpenJDK version 1.7.0 (64 bit)"]]]]])
