(ns app.name-picker
  (:require [goog.dom :as gdom]
            [app.lib :as lib]
            [reagent.core :as r]))

(defn letter-elem
  [letter name]
  [:li
   ; {:on-click (fn []
   ;              (swap! name
   ;                     (fn [current-name]
   ;                       (conj current-name letter))))}
   letter])

(defn letters-list
  "Makes list of letters"
  [name]
  [:ul {:id "letters"}
       (for [letter lib/letters]
         ^{:key letter} [letter-elem letter name])])

(defn draw-name-picker
  "Draw list of names"
  []
  (let [name (r/atom [:0])]
       [letters-list name]
       [:p @name]))
