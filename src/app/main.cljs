(ns app.main
  (:require [goog.dom :as gdom]
            [goog.events :as gevents]
            [app.lib :as lib]))

(defrecord Game [names])

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
  [ctx {:keys [rects]}]
  (doseq [rect rects]
    (draw-rect! ctx rect)))

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
  [width height]
  (let [base (quot height 3)
        total-h (* base 3)
        total-w (* base 4)
        rect-h (quot total-h 9)
        rect-w (quot total-w 9)
        vert-diff (- height total-h)
        horz-diff (- width total-w)
        offset-x (quot horz-diff 2)
        offset-y (quot vert-diff 2)]
    (Screen. (gen-rects 9 rect-w rect-h offset-x offset-y))))

(def game (atom {:game (Game. [])
                 :screen nil}))

(defn reload!
  "Function that's called when the code is reloaded"
  []
  (let [canvas (.getElementById js/document "game")
        ctx (gdom/getCanvasContext2D canvas)]
    (draw-game! ctx (:screen game))))

(lib/properties [:0 :A :A])

(defn main!
  "Main function"
  []
  (let [body js/document.body
        window-width js/window.innerWidth
        window-height js/window.innerHeight
        canvas (new-canvas "game" window-width window-height)
        ctx (gdom/getCanvasContext2D canvas)
        scr (gen-screen window-width window-height)]
    (gdom/appendChild body canvas)
    (swap! game assoc :screen scr)
    (set! (.-fillStyle ctx) "#AAAAAA")
    (set! (.-strokeStyle ctx) "#FFFFFF")
    (gevents/listen js/window
                    "resize"
                    (fn []
                      (let [w js/window.innerWidth
                            h js/window.innerHeight
                            scr (gen-screen w h)]
                        (resize-canvas! canvas w h)
                        (swap! game assoc :screen scr)
                        (set! (.-fillStyle ctx) "#AAAAAA")
                        (set! (.-strokeStyle ctx) "#FFFFFF")
                        (draw-game! ctx (:screen @game)))))
    (draw-game! ctx (:sreen @game))))
