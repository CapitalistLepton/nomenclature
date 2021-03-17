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
(ns app.game
  (:require [app.cells :as cells]))

(defn gen-game
  "Generates the game"
  [size]
  (map (fn [x]
         (if (< (quot x size) 2)
           (cells/make-entity [:A :A :A] :player1 :clay)
           (if (> (quot x size) 7)
             (cells/make-entity [:A :A :A] :player2 :clay)
             nil)))
       (range 0 (* size size))))

(gen-game 9)

(defn transition-game
  "Returns a new game state based on the given state"
  [prev-game size]
  (-> prev-game
      (cells/move-cells size)
      (cells/filter-collisions size)
      (cells/handle-collisions)))
