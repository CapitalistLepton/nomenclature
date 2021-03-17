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
(ns app.cells
  (:require [app.lib :as lib]))

(defn make-entity
  "Make an entity from a name, owner and form"
  [name owner form]
  {:name  name
   :owner owner
   :form  form})

(defn move-index
  "Calculate the next index from the current index, the move, and the width/height of the grid"
  [index [dx dy] size]
  (let [x (mod index size)
        y (quot index size)
        nx (+ x dx)
        ny (+ y dy)]
    (mod (+ (* ny size) nx) (* size size))))

(defn move-cells
  "Moves the cells in the game. Returns a list of entities and their indexes"
  [prev-game size]
  (map-indexed (fn [i entity]
                 (if (not (nil? entity))
                   (let [move (lib/movement (:name entity))
                         ni (move-index i move size)]
                     [ni entity])
                   [i entity]))
               prev-game))

(defn filter-collisions
  "Group cells by their index. Collided cells will be in a list"
  [indexed-cells size]
  (reduce (fn [cell-map [i cell]]
            (if (not (nil? cell))
              (assoc cell-map i (cons cell (get cell-map i)))
              cell-map))
          (vec (repeat (* size size) '()))
          indexed-cells))

(defn handle-collisions
  "Handle cells that have collided. Return list of all cells, nil where there are no names."
  [cells]
  (map (fn [cell-list]
         (if (= cell-list '())
           nil
           (first cell-list))) ; TODO replace with actual collision logic
       cells))
