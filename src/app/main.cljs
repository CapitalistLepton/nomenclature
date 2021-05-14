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
  (:require [reagent.dom :as rdom]
            [app.lib :as lib]
            [app.name-picker :as name-picker]
            [app.board :as board]))

(defonce seed 123456789)

(defonce prng (lib/init-rng seed))

(defn draw-page
  "Draw the given page inside the main container"
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
                                              (fn [] (draw-page home-page))
                                              (take 7 (repeatedly (partial lib/random-letter prng)))))}
      "Name Picker"]]
    [:li
     [:button {:on-click #(rdom/render [:canvas {:id "board"}]
                                       (.getElementById js/document "container")
                                       board/board-init-hook)}
      "Play"]]]])

(defn reload!
  "Function that's called when the code is reloaded"
  []
  (draw-page home-page))

(defn main!
  "Main function"
  []
    ;(do
    ;  (lib/next-rng prng)
    ;  (println (lib/get-double prng)))
    ;(do
    ;  (lib/next-rng prng)
    ;  (println "Test 2 " (lib/get-double prng)))
    ;(println
    ;  (for [i (range 10)]
    ;    (str "Letter " i " " (lib/random-letter prng))))
    ;)
    (draw-page home-page))
