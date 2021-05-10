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
(ns app.rng)

(def A 11)
(def B 15)
(def C 37)

(defn gen-random
  "Generates a new random atom"
  [seed]
  (atom seed))

(defn xor-shift
  "Perform XOR shift on given 64-bit number"
  [x]
  (as-> x x
    (bit-xor x (bit-shift-left x A))
    (bit-xor x (unsigned-bit-shift-right x B))
    (bit-xor x (bit-shift-left x C))))

(defn next!
  "Get the next 64-bit random number"
  [rng]
  (swap! rng xor-shift))

(defn to-double
  "Converts a long to a double over [0,1)"
  [x]
  (let [imax #?(:clj java.lang.Long/MAX_VALUE
                :cljs js/Number.MAX_SAFE_INTEGER)
        imin #?(:clj java.lang.Long/MIN_VALUE
                :cljs js/Number.MIN_SAFE_INTEGER)]
    (println "^ " imax " v " imin)
    (println "1 " (- x imin))
    (println "2 " (- imax imin))
    (double (quot (- x imin)
                  (- imax imin)))))
