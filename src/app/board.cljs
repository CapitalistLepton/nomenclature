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
(ns app.board)

(defn square-elem
  "Displays a square on the board"
  [i]
  [:button
   {:id (str "square-" i)
    :on-click (fn [i]
                (println "Hello" (.-id (.-target i))))}
   i])

(defn draw-board
  "Draw the board where the game is played"
  [home-fn]
  [:div
   [:button {:on-click home-fn}
    "Back"]
   [:h1 "Board"]
   [:div
    {:id "game-board"}
    (for [i (range 25)]
      ^{:key i} [square-elem i])]])
