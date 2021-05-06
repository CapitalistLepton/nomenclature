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
            [reagent.dom :as rdom]
            [app.lib :as lib]
            [app.game :as game]
            [app.name-picker :as name-picker]
            [app.board :as board]))

;(defrecord Screen [rects])
;
;(defrecord Rect [x y w h])
;
;(defn new-canvas
;  "Creates and returns a new canvas element with the specified ID, width, and height"
;  [id width height]
;  (let [canvas (gdom/createElement "canvas")
;        properties #js{"id"     id
;                       "width"  width
;                       "height" height}]
;    (gdom/setProperties canvas properties)
;    canvas))
;
;
;(defn draw-rect!
;  "Draws a rectangle on the given context"
;  [ctx {:keys [x y w h]}]
;  (.strokeRect ctx (- x 1) (- y 1) (- w 2) (- h 2))
;  (.fillRect ctx x y w h))
;
;(defn color-rect!
;  "Draws a rectangle with the given fill color"
;  [ctx rect color]
;  (if (nil? color)
;    (draw-rect! ctx rect)
;    (do
;      (.save ctx)
;      (set! (.-fillStyle ctx) color)
;      (draw-rect! ctx rect)
;      (.restore ctx))))
;
;(defn draw-game!
;  "Draws the contents of the game"
;  [ctx {:keys [game screen]}]
;  (doseq [[rect entity] (map list (:rects screen) game)]
;    (if (nil? entity)
;      (color-rect! ctx rect "#FFFFFF")
;      (color-rect! ctx rect (lib/color (:name entity))))))
;
;(defn gen-rects
;  "Generates a list of size^2 rectangles with the given width and height"
;  [size width height offset-x offset-y]
;  (map (fn [x]
;         (Rect. (+ (* (mod x size) width) offset-x)
;                (+ (* (quot x size) height) offset-y)
;                width
;                height))
;       (range 0 (* size size))))
;
;(defn gen-screen
;  "Generates the screen object based on the full width and height"
;  [width height size]
;  (let [base (quot height 3)
;        total-h (* base 3)
;        total-w (* base 4)
;        rect-h (quot total-h size)
;        rect-w (quot total-w size)
;        vert-diff (- height total-h)
;        horz-diff (- width total-w)
;        offset-x (quot horz-diff 2)
;        offset-y (quot vert-diff 2)]
;    (Screen. (gen-rects size rect-w rect-h offset-x offset-y))))
;
;(defn init-screen
;  "Creates the initial screen object"
;  [size]
;  (let [window-width js/window.innerWidth
;        window-height js/window.innerHeight]
;    (gen-screen window-width window-height size)))
;
;(defn init-canvas
;  "Creates the initial canvas based on the window measurements"
;  []
;  (let [body js/document.body
;        window-width js/window.innerWidth
;        window-height js/window.innerHeight
;        canvas (new-canvas "game" window-width window-height)
;        ctx (gdom/getCanvasContext2D canvas)]
;    (println "Create canvas " canvas)
;    (gdom/appendChild body canvas)
;    ctx))
;
;(defn prompt-user
;  [prmpt regex]
;  (loop [response (js/prompt prmpt "")]
;    (if (not (nil? (re-matches regex response)))
;      response
;      (recur (js/prompt (str "Invalid input. " prmpt) "")))))
;
;(defn clear-body
;  []
;  (let [body js/document.body]
;    (gdom/removeChildren body)))
;
;(def SIZE 9)
;
;(def state (atom {:game nil
;                 :screen nil}))
;
;(defn handle-resize
;  "Resize the canvas as the window is resized"
;  []
;  (let [w js/window.innerWidth
;        h js/window.innerHeight
;        canvas (.getElementById js/document "game")
;        ctx (gdom/getCanvasContext2D canvas)
;        scr (gen-screen w h SIZE)]
;    (resize-canvas! canvas w h)
;    (swap! state assoc :screen scr)
;    (draw-game! ctx @state)))
;
;(defn game-loop
;  "Actions to perform in the game loop"
;  []
;  (let [canvas (.getElementById js/document "game")
;        ctx (if (not (nil? canvas))
;              (gdom/getCanvasContext2D canvas)
;              nil)]
;    (if (nil? ctx)
;      (println "Canvas is null")
;      (do
;        (swap! state assoc :game (game/transition-game (:game @state) SIZE))
;        (let [counts (game/count-entities (:game @state))]
;          (draw-game! ctx @state)
;          (if (or (= (:player1 counts) 0)
;                  (= (:player2 counts) 0))
;            (if (= (:player1 counts) 0)
;              (js/alert "Player 2 won")
;              (js/alert "Player 1 won"))
;            nil)))))) ; No winners yet

;(defn reload!
;  "Function that's called when the code is reloaded"
;  []
;  (let [canvas (.getElementById js/document "game")
;        ctx (gdom/getCanvasContext2D canvas)]
;    (draw-game! ctx @state)))

(defn draw-page
  [page-cmp]
  (rdom/render
    [page-cmp]
    (.getElementById js/document "container")))

(defn home-page
  []
  [:div
   [:h1 "Home Page"]
   [:ul
    [:li
     [:button {:on-click #(draw-page (partial name-picker/draw-name-picker
                                              (fn [] (draw-page home-page))))}
      "Name Picker"]]
    [:li
     [:button {:on-click #(rdom/render [:canvas {:id "board"}]
                                       (.getElementById js/document "container")
                                       board/board-init-hook)}
      "Play"]]]])

(defn main!
  "Main function"
  []
  (draw-page home-page))
  ; (let [ctx (init-canvas)]
  ;   (swap! state assoc :screen (init-screen SIZE))
  ;   (gevents/listen js/window "resize" handle-resize)
  ;   (let [name1 (prompt-user "Enter name (Player 1)" #"^[0-9ab]{3}$")
  ;         parsed-name1 (lib/parse-name name1)
  ;         name2 (prompt-user "Enter name (Player 2)" #"^[0-9ab]{3}$")
  ;         parsed-name2 (lib/parse-name name2)]
  ;     (println parsed-name1 ":" parsed-name2)
  ;     (swap! state assoc :game (game/gen-game SIZE parsed-name1 parsed-name2))
  ;     (draw-game! ctx @state)
  ;     (js/setInterval game-loop 1000))))
