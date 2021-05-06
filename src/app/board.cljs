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
(ns app.board
  (:require [goog.dom :as gdom]
            [goog.events :as gevents]))


(defn resize-canvas!
  "Resizes the canvas to fit the screen"
  [canvas-id]
  (let [canvas (.getElementById js/document canvas-id)
        width js/window.innerWidth
        height js/window.innerHeight]
    (println "W " width " H " height)
    (gdom/setProperties canvas #js{"width" width
                                   "height" height})))


(defn board-init-hook
  "Function to call when the canvas has been attached"
  []
  (println "Attached canvas")
  (gevents/listen js/window "resize" (partial resize-canvas! "board"))
  (resize-canvas! "board"))
