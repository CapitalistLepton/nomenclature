(ns app.name-picker
  (:require [goog.dom :as gdom]
            [app.lib :as lib]
            [reagent.core :as r]))

(defonce name-picked (r/atom [:0]))

(defn letter-elem
  [letter name]
  [:li
   [:button
    {:on-click (fn []
                 (when (< (count @name) 3)
                   (swap! name conj letter))
                 (println @name))}
    letter]])

(defn letters-list
  "Makes list of letters"
  [name]
  [:ul {:id "letters"}
   (for [letter lib/letters]
     ^{:key letter} [letter-elem letter name])
   [:li
    [:button {:on-click (fn []
                     (when (> (count @name) 0)
                       (swap! name pop))
                     (println @name))}
     "Delete"]]])

(defn draw-name-picker
  "Draw list of names"
  []
  [:div
   "Name: " (str @name-picked)
   [letters-list name-picked]])
