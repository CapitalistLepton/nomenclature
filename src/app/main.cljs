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
(ns app.main
  (:require [goog.dom :as gdom]
            [goog.events :as gevents]
            [app.lib :as lib]))

(def SIZE 9)

(defrecord Screen [rects])

(defrecord Rect [x y w h])

(defn new-canvas
  "Creates and returns a new canvas element with the specified ID, width, and height"
  [id width height]
  (let [canvas (gdom/createElement "canvas")
        properties #js{"id"     id
                       "width"  width
                       "height" height}]
    (gdom/setProperties canvas properties)
    canvas))

(defn resize-canvas!
  "Resizes the canvas to fit the screen"
  [canvas width height]
  (gdom/setProperties canvas #js{"width"  width
                                 "height" height}))

(defn draw-rect!
  "Draws a rectangle on the given context"
  [ctx {:keys [x y w h]}]
  (.strokeRect ctx (- x 1) (- y 1) (- w 2) (- h 2))
  (.fillRect ctx x y w h))

(defn color-rect!
  "Draws a rectangle with the given fill color"
  [ctx rect color]
  (if (nil? color)
    (draw-rect! ctx rect)
    (do
      (.save ctx)
      (set! (.-fillStyle ctx) color)
      (draw-rect! ctx rect)
      (.restore ctx))))

(defn draw-game!
  "Draws the contents of the game"
  [ctx {:keys [game screen]}]
  (doseq [[rect name] (map list (:rects screen) (:names game))]
    (if (= name nil)
      (color-rect! ctx rect "#FFFFFF")
      (color-rect! ctx rect (lib/color name)))))

(defn gen-rects
  "Generates a list of size^2 rectangles with the given width and height"
  [size width height offset-x offset-y]
  (map (fn [x]
         (Rect. (+ (* (mod x size) width) offset-x)
                (+ (* (quot x size) height) offset-y)
                width
                height))
       (range 0 (* size size))))

(defn gen-screen
  "Generates the screen object based on the full width and height"
  [width height size]
  (let [base (quot height 3)
        total-h (* base 3)
        total-w (* base 4)
        rect-h (quot total-h size)
        rect-w (quot total-w size)
        vert-diff (- height total-h)
        horz-diff (- width total-w)
        offset-x (quot horz-diff 2)
        offset-y (quot vert-diff 2)]
    (Screen. (gen-rects SIZE rect-w rect-h offset-x offset-y))))

(defn gen-game
  "Generates the game"
  [size]
  {:names (map (fn [x]
                (if (= (quot x size) 5)
                  [:4 :A :A]
                  nil))
              (range 0 (* size size)))})

(defn move-index
  "Calculate the next index from the current index, the move, and the widht/height of the grid"
  [index [dx dy] size]
  (let [x (mod index size)
        y (quot index size)
        nx (+ x dx)
        ny (+ y dy)]
    (mod (+ (* ny size) nx) (* size size))))

(defn move-cells
  "Moves the cells in the game. Returns a list of names and their indexes"
  [prev-game size]
  (map-indexed (fn [i name]
                 (if (not (nil? name))
                   (let [move (lib/movement name)
                         ni (move-index i move size)]
                     [ni name])
                   [i name]))
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

(defn transition-game
  "Returns a new game state based on the given state"
  [prev-game size]
  (let [new-names (-> prev-game
                      (:names)
                      (move-cells size)
                      (filter-collisions size)
                      (handle-collisions))]
    (assoc prev-game :names new-names)))

(def game (atom {:game (gen-game SIZE)
                 :screen nil}))

(defn reload!
  "Function that's called when the code is reloaded"
  []
  (let [canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)]
    (draw-game! ctx @game)))

(defn main!
  "Main function"
  []
  (let [body js/document.body
        window-width js/window.innerWidth
        window-height js/window.innerHeight
        canvas (new-canvas "game" window-width window-height)
        ctx (gdom/getCanvasContext2D canvas)
        scr (gen-screen window-width window-height SIZE)]
    (gdom/appendChild body canvas)
    (swap! game assoc :screen scr)
    (gevents/listen js/window
                    "resize"
                    (fn []
                      (let [w js/window.innerWidth
                            h js/window.innerHeight
                            scr (gen-screen w h SIZE)]
                        (resize-canvas! canvas w h)
                        (swap! game assoc :screen scr)
                        (draw-game! ctx @game))))
    (draw-game! ctx @game)
    (js/setInterval (fn []
                      (swap! game assoc :game (transition-game (:game @game) SIZE))
                      (draw-game! ctx @game))
                    1000)))
