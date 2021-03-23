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

(defn new-text
  "Creates a new text element"
  [id body]
  (let [p (gdom/createElement "p")
        properties #js{"id" id
                       "innerHTML" body}]
    (gdom/setProperties p properties)
    p))

(defn new-input
  "Creates a new text input field"
  [id]
  (let [input (gdom/createElement "input")
        properties #js{"id" id}]
    (gdom/setProperties input properties)
    input))

(defn new-button
  "Creates a new button"
  [id text]
  (let [button (gdom/createElement "button")
        properties #js{"id" id
                       "innerHTML" text}]
    (gdom/setProperties button properties)
    button))

(defn new-div
  "Creates a new div container"
  [id margin-left margin-top]
  (let [div (gdom/createElement "div")
        style (str "margin-top: " margin-top "; margin-left: " margin-left)
        properties #js{"id" id
                       "style" style}]
    (gdom/setProperties div properties)
    div))

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
    (Screen. (gen-rects size rect-w rect-h offset-x offset-y))))

(defn init-screen
  "Creates the initial screen object"
  [size]
  (let [window-width js/window.innerWidth
        window-height js/window.innerHeight]
    (gen-screen window-width window-height size)))

(defn init-canvas
  "Creates the initial canvas based on the window measurements"
  []
  (let [body js/document.body
        window-width js/window.innerWidth
        window-height js/window.innerHeight
        canvas (new-canvas "game" window-width window-height)
        ctx (gdom/getCanvasContext2D canvas)]
    (gdom/appendChild body canvas)
    ctx))

(defn prompt-user
  [prompt click-handler]
  (let [body js/document.body
        container (new-div "prompt-container" "40%" "10%")
        prompt-text (new-text "prompt-text" prompt)
        input (new-input "prompt-input")
        submit (new-button "prompt-submit" "Enter")]
    (gdom/appendChild body container)
    (gdom/appendChild container prompt-text)
    (gdom/appendChild container input)
    (gevents/listen submit "click" click-handler)
    (gdom/appendChild container submit)))

(defn clear-body
  []
  (let [body js/document.body]
    (gdom/removeChildren body)))

(def SIZE 9)

(def state (atom {:game (game/gen-game SIZE)
                 :screen nil}))

(defn handle-resize
  "Resize the canvas as the window is resized"
  []
  (let [w js/window.innerWidth
        h js/window.innerHeight
        canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)
        scr (gen-screen w h SIZE)]
    (resize-canvas! canvas w h)
    (swap! state assoc :screen scr)
    (draw-game! ctx @state)))

(defn game-loop
  "Actions to perform in the game loop"
  []
  (let [canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)]
    (swap! state assoc :game (game/transition-game (:game @state) SIZE))
    (draw-game! ctx @state)))

(defn reload!
  "Function that's called when the code is reloaded"
  []
  (let [canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)]
    (draw-game! ctx @state)))

(defn main!
  "Main function"
  []
  ;(let [ctx (init-canvas)]
    (clear-body)
    (swap! state assoc :screen (init-screen SIZE))
    (gevents/listen js/window "resize" handle-resize)
    (let [name1 (js/prompt "Enter name (Player 1)" "")
          name2 (js/prompt "Enter name (Player 2)" "")]
      (println name1 ":" name2))
    )
    ;(draw-game! ctx @state)
    ;(js/setInterval game-loop 1000)))
