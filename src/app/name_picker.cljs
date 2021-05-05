(ns app.name-picker
  (:require [goog.dom :as gdom]
            [app.lib :as lib]
            [reagent.core :as r]))

(defonce name-picked (r/atom []))

(defn letter-elem
  [letter name-ref]
  [:li
   [:button
    {:on-click (fn []
                 (when (< (count @name-ref) 3)
                   (swap! name-ref conj letter))
                 (println @name-ref))}
    letter]])

(defn letters-list
  "Makes list of letters"
  [name-ref]
  [:ul {:id "letters"}
   (for [letter lib/letters]
     ^{:key letter} [letter-elem letter name-ref])
   [:li
    [:button {:on-click (fn []
                     (when (> (count @name-ref) 0)
                       (swap! name-ref pop))
                     (println @name-ref))}
     "Delete"]]])

(defn name-properties
  "Display properties for the 3 letter name that was picked"
  [name]
  (when (= (count name) 3)
    (let [properties (lib/properties name)]
      [:div
       [:p "Properties for " (str name)]
       [:p {:style {:color (:color properties)}}
        "Color"]
       [:p "Speed: " (:speed properties)]
       [:p "Strength: " (:strength properties)]
       [:p "Health: " (:health properties)]])))

(defn draw-name-picker
  "Draw list of names"
  []
  [:div
   "Name: " (str @name-picked)
   [letters-list name-picked]
   [name-properties @name-picked]])
