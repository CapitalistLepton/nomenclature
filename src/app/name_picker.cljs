;; Copyright 2021 Zane Littrell
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
(ns app.name-picker
  (:require [app.lib :as lib]
            [reagent.core :as r]))

(defonce state (r/atom []))

(defn letter-elem
  "Display a letter as a button which adds the letter to the name when pressed"
  [letter state]
  [:li
   [:button
    {:on-click (fn []
                 (when (< (count @state) 3)
                   (swap! state conj letter))
                 (println @state))}
    letter]])

(defn letters-list
  "Makes list of letters"
  [letters state]
  [:ul {:id "letters"}
   (map-indexed (fn [i letter]
                  ^{:key (str letter "-" i)} [letter-elem letter state])
                letters)
   [:li
    [:button {:on-click (fn []
                          (when (> (count @state) 0)
                            (swap! state (fn [old-state]
                                           (pop old-state))))
                          (println @state))}
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
  [home-fn possible-letters]
  (println possible-letters)
  (let [letters (reduce (fn [letters letter]
                          (lib/remove-letter letters letter))
                        possible-letters
                        @state)]
    (println letters)
    [:div
     [:button {:on-click home-fn}
      "Back"]
     [name-properties @state]
     [:p "Name: " (str @state)]
     [letters-list letters state]]))
