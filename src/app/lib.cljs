(ns app.lib)

(def letters {:0 0
              :1 1
              :2 2
              :3 3
              :4 4
              :5 5
              :6 6
              :7 7
              :8 8
              :9 9
              :A 10
              :B 11})

(def irrationals ["141421356237" ;sqrt 2
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
  [name]
  (case (get name 1)
    (:0 :1 :2 :3) 0.25
    (:4 :5 :6 :7) 0.5
    (:8 :9 :A :B) 0.75))

(defn strength
  [name]
  (-> irrationals
      (get (get letters (get name 0)))
      (get (get letters (get name 2)))
      (js/parseInt 10)))

(defn health
  [name]
  (-> irrationals
      (get (get letters (get name 0)))
      (get (get letters (get name 1)))
      (js/parseInt 10)))

(defn properties
  [name]
  {:color    (color name)
   :speed    (speed name)
   :strength (strength name)
   :health   (health name)})

(properties [:0 :4 :0])
