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
(ns app.lib)

(def letters
  "List of all the possible lettters"
  [:0
   :1
   :2
   :3
   :4
   :5
   :6
   :7
   :8
   :9
   :A
   :B])

(def irrationals
  "List of 12 irrational numbers"
  ["141421356237" ;sqrt 2
   "161803398874" ;phi (golden ratio)
   "173205080756" ;sqrt 3
   "223606797749" ;sqrt 5
   "264575131106" ;sqrt 7
   "271828182845" ;e
   "282842712474" ;sqrt 8
   "314159265358" ;pi
   "316227766016" ;sqrt 10
   "331662479035" ;sqrt 11
   "346410161513" ;sqrt 12
   "360555127546" ;sqrt 13
   ])

(defn color
  "Returns the color of the name"
  [name]
  (case (get name 0)
    :0 "#800000"
    :1 "#008000"
    :2 "#808000"
    :3 "#000080"
    :4 "#800080"
    :5 "#008080"
    :6 "#c0c0c0"
    :7 "#808080"
    :8 "#ff0000"
    :9 "#00ff00"
    :A "#ffff00"
    :B "#0000ff"))

(defn speed
  "Returns the speed stat of the name"
  [name]
  (case (get name 1)
    (:0 :1 :2 :3) 0.25
    (:4 :5 :6 :7) 0.5
    (:8 :9 :A :B) 0.75))

(defn strength
  "Returns the strength stat of the name"
  [name]
  (-> irrationals
      (get (get letters (get name 0)))
      (get (get letters (get name 2)))
      (int)))

(defn health
  "Returns the health stat of the name"
  [name]
  (-> irrationals
      (get (get letters (get name 0)))
      (get (get letters (get name 1)))
      (int)))

(defn properties
  "Returns a map of the properties of the name"
  [name]
  {:color    (color name)
   :speed    (speed name)
   :strength (strength name)
   :health   (health name)})

(defn movement
  "Returns the next move of the name"
  [name]
  (case (get name 0)
    :0 [ 0  1]
    :1 [ 0 -1] 
    :2 [ 1  0] 
    :3 [-1  0] 
    :4 [ 1  1] 
    :5 [ 1 -1] 
    :6 [-1  1] 
    :7 [-1 -1] 
    :8 [ 0  1] 
    :9 [ 0 -1] 
    :A [ 1  0] 
    :B [-1  0]))

(defn rand-letter
  "Gets a random letter from the possible letters"
  []
  (rand-nth letters))
