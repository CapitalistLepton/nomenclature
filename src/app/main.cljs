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
            [app.lib :as lib]
            [app.game :as game]))

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
  (doseq [[rect entity] (map list (:rects screen) game)]
    (if (nil? entity)
      (color-rect! ctx rect "#FFFFFF")
      (color-rect! ctx rect (lib/color (:name entity))))))

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

(def state (atom {:game (game/gen-game SIZE)
                 :screen nil}))

(defn reload!
  "Function that's called when the code is reloaded"
  []
  (let [canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)]
    (draw-game! ctx @state)))

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
    (swap! state assoc :screen scr)
    (gevents/listen js/window
                    "resize"
                    (fn []
                      (let [w js/window.innerWidth
                            h js/window.innerHeight
                            scr (gen-screen w h SIZE)]
                        (resize-canvas! canvas w h)
                        (swap! state assoc :screen scr)
                        (draw-game! ctx @state))))
    (draw-game! ctx @state)
    (js/setInterval (fn []
                      (swap! state assoc :game (game/transition-game (:game @state) SIZE))
                      (draw-game! ctx @state))
                    1000)))
