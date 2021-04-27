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
(ns app.stats
  (:require [app.lib :as lib]))

; NOTE: This file is strictly for observing the property of names
; Any code in this file should not be referenced by any other file

(def strong-letters
  [:1
   :3
   :5
   :7
   :8
   :9
   :B])

(def weak-letters
  [:0
   :2
   :4
   :6
   :A])

(def all-names
  (apply concat
         (mapcat concat
                 (for [i (range 12)]
                   (for [j (range 12)]
                     (for [k (range 12)]
                       [(nth lib/letters i)
                        (nth lib/letters j)
                        (nth lib/letters k)]))))))

(def weak-names
  (apply concat
         (mapcat concat
                 (for [i (range (count weak-letters))]
                   (for [j (range (count weak-letters))]
                     (for [k (range (count weak-letters))]
                       [(nth weak-letters i)
                        (nth weak-letters j)
                        (nth weak-letters k)]))))))

(def sorted-power (sort-by (fn [x]
                             (+ (first x) (second x)))
                           (map (fn [name]
                                  (list (lib/strength name) (lib/health name) name))
                                all-names)))

(def sorted-weak (sort-by (fn [x]
                             (+ (first x) (second x)))
                           (map (fn [name]
                                  (list (lib/strength name) (lib/health name) name))
                                weak-names)))

(take-last 12 sorted-weak)

(count (filter (fn [x]
                 (and (>= (+ (first x) (second x)) 10)
                      (or (>= (first x) 5)
                          (>= (second x) 5))))
               sorted-power))

(filter (fn [x]
          (= (+ (first x) (second x)) 18))
          sorted-power)
