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
  [home-fn]
  [:div
   [:button {:on-click home-fn}
    "Back"]
   [:p "Name: " (str @name-picked)]
   [letters-list name-picked]
   [name-properties @name-picked]])
