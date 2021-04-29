(ns app.name-picker
  (:require [goog.dom :as gdom]
            [app.lib :as lib]))

(defn basic-element
  [element-name innerHTML]
  (let [elem (gdom/createElement element-name)
        properties #js{"innerHTML" innerHTML}]
    (gdom/setProperties elem properties)
    elem))

(defn ul-element
  [id children]
  (let [ul (gdom/createElement "ul")
        properties #js{"id" id}]
    (gdom/setProperties ul properties)
    (doseq [child children]
      (gdom/appendChild ul child))
    ul))

(defn li-element
  [innerHTML]
  (basic-element "li" innerHTML))

(defn names-list
  "Makes list of names"
  []
  (ul-element "names"
              (map (fn [x] (li-element x))
                   lib/letters)))

(defn draw-name-picker
  "Draw list of names"
  []
  (let [body js/document.body
        names (names-list)]
    (gdom/appendChild body names)))
