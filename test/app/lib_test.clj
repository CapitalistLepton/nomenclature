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
(ns app.lib-test
  (:require [clojure.test :refer [deftest is testing]]
            [xoroshiro128.core :as rng]
            [app.lib :as lib]))

(deftest color-test
  (testing "Color for :0"
    (is (= "#800000" (lib/color [:0 nil nil]))))
  (testing "Color for :1"
    (is (= "#008000" (lib/color [:1 nil nil]))))
  (testing "Color for :2"
    (is (= "#808000" (lib/color [:2 nil nil]))))
  (testing "Color for :3"
    (is (= "#000080" (lib/color [:3 nil nil]))))
  (testing "Color for :4"
    (is (= "#800080" (lib/color [:4 nil nil]))))
  (testing "Color for :5"
    (is (= "#008080" (lib/color [:5 nil nil]))))
  (testing "Color for :6"
    (is (= "#c0c0c0" (lib/color [:6 nil nil]))))
  (testing "Color for :7"
    (is (= "#808080" (lib/color [:7 nil nil]))))
  (testing "Color for :8"
    (is (= "#ff0000" (lib/color [:8 nil nil]))))
  (testing "Color for :9"
    (is (= "#00ff00" (lib/color [:9 nil nil]))))
  (testing "Color for :A"
    (is (= "#ffff00" (lib/color [:A nil nil]))))
  (testing "Color for :B"
    (is (= "#0000ff" (lib/color [:B nil nil]))))
  (testing "Color for nil"
    (let [exception (atom false)]
      (try
        (lib/color [nil nil nil])
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest speed-test
  (testing "Speed for :0"
    (is (= 0.25 (lib/speed [nil :0 nil]))))
  (testing "Speed for :1"
    (is (= 0.25 (lib/speed [nil :1 nil]))))
  (testing "Speed for :2"
    (is (= 0.25 (lib/speed [nil :2 nil]))))
  (testing "Speed for :3"
    (is (= 0.25 (lib/speed [nil :3 nil]))))
  (testing "Speed for :4"
    (is (= 0.5 (lib/speed  [nil :4 nil]))))
  (testing "Speed for :5"
    (is (= 0.5 (lib/speed  [nil :5 nil]))))
  (testing "Speed for :6"
    (is (= 0.5 (lib/speed  [nil :6 nil]))))
  (testing "Speed for :7"
    (is (= 0.5 (lib/speed  [nil :7 nil]))))
  (testing "Speed for :8"
    (is (= 0.75 (lib/speed [nil :8 nil]))))
  (testing "Speed for :9"
    (is (= 0.75 (lib/speed [nil :9 nil]))))
  (testing "Speed for :A"
    (is (= 0.75 (lib/speed [nil :A nil]))))
  (testing "Speed for :B"
    (is (= 0.75 (lib/speed [nil :B nil]))))
  (testing "Speed for nil"
    (let [exception (atom false)]
      (try
        (lib/speed [nil nil nil])
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest parse-keyword-test
  (testing "Value of :0"
    (is (= 0 (lib/parse-keyword :0))))
  (testing "Value of :1"
    (is (= 1 (lib/parse-keyword :1))))
  (testing "Value of :2"
    (is (= 2 (lib/parse-keyword :2))))
  (testing "Value of :3"
    (is (= 3 (lib/parse-keyword :3))))
  (testing "Value of :4"
    (is (= 4 (lib/parse-keyword :4))))
  (testing "Value of :5"
    (is (= 5 (lib/parse-keyword :5))))
  (testing "Value of :6"
    (is (= 6 (lib/parse-keyword :6))))
  (testing "Value of :7"
    (is (= 7 (lib/parse-keyword :7))))
  (testing "Value of :8"
    (is (= 8 (lib/parse-keyword :8))))
  (testing "Value of :9"
    (is (= 9 (lib/parse-keyword :9))))
  (testing "Value of :A"
    (is (= 10 (lib/parse-keyword :A))))
  (testing "Value of :B"
    (is (= 11 (lib/parse-keyword :B))))
  (testing "Value of nil"
    (let [exception (atom false)]
      (try
        (lib/parse-keyword nil)
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest strength-test
  (testing "Strength for :0 .. :0"
    (is (= 1 (lib/strength [:0 nil :0]))))
  (testing "Strength of nil"
    (let [exception (atom false)]
      (try
        (lib/strength [nil nil nil])
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest health-test
  (testing "Health for :0 :0 .."
    (is (= 1 (lib/health [:0 :0 nil]))))
  (testing "Health of nil"
    (let [exception (atom false)]
      (try
        (lib/health [nil nil nil])
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest properties-test
  (testing "Properties for :0 :0 :0"
    (let [expected {:color "#800000"
                    :speed 0.25
                    :strength 1
                    :health 1}]
      (is (= expected (lib/properties[:0 :0 :0])))))
  (testing "Properties of nil"
    (let [exception (atom false)]
      (try
        (lib/properties [nil nil nil])
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest movement-test
  (testing "Movement of :0"
    (is (= [0 1] (lib/movement [:0 nil nil]))))
  (testing "Movement of :1"
    (is (= [0 -1] (lib/movement [:1 nil nil]))))
  (testing "Movement of :2"
    (is (= [1 0] (lib/movement [:2 nil nil]))))
  (testing "Movement of :3"
    (is (= [-1 0] (lib/movement [:3 nil nil]))))
  (testing "Movement of :4"
    (is (= [1 1] (lib/movement [:4 nil nil]))))
  (testing "Movement of :5"
    (is (= [1 -1] (lib/movement [:5 nil nil]))))
  (testing "Movement of :6"
    (is (= [-1 1] (lib/movement [:6 nil nil]))))
  (testing "Movement of :7"
    (is (= [-1 -1] (lib/movement [:7 nil nil]))))
  (testing "Movement of :8"
    (is (= [0 1] (lib/movement [:8 nil nil]))))
  (testing "Movement of :9"
    (is (= [0 -1] (lib/movement [:9 nil nil]))))
  (testing "Movement of :A"
    (is (= [1 0] (lib/movement [:A nil nil]))))
  (testing "Movement of :B"
    (is (= [-1 0] (lib/movement [:B nil nil]))))
  (testing "Movement of nil"
    (let [exception (atom false)]
      (try
        (lib/movement nil)
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest parse-letter-test
  (testing "Letter of 0"
    (is (= :0 (lib/parse-letter \0))))
  (testing "Letter of 1"
    (is (= :1 (lib/parse-letter \1))))
  (testing "Letter of 2"
    (is (= :2 (lib/parse-letter \2))))
  (testing "Letter of 3"
    (is (= :3 (lib/parse-letter \3))))
  (testing "Letter of 4"
    (is (= :4 (lib/parse-letter \4))))
  (testing "Letter of 5"
    (is (= :5 (lib/parse-letter \5))))
  (testing "Letter of 6"
    (is (= :6 (lib/parse-letter \6))))
  (testing "Letter of 7"
    (is (= :7 (lib/parse-letter \7))))
  (testing "Letter of 8"
    (is (= :8 (lib/parse-letter \8))))
  (testing "Letter of 9"
    (is (= :9 (lib/parse-letter \9))))
  (testing "Letter of a"
    (is (= :A (lib/parse-letter \a))))
  (testing "Letter of b"
    (is (= :B (lib/parse-letter \b))))
  (testing "Letter of nil"
    (let [exception (atom false)]
      (try
        (lib/parse-letter nil)
        (catch IllegalArgumentException _ (compare-and-set! exception false true)))
    (is (= true @exception)))))

(deftest parse-name-test
  (testing "Name of \"8ab\""
    (is (= [:8 :A :B] (lib/parse-name "8ab")))))

(deftest init-rng-test
  (testing "Creates an atom with the specified seed"
    (let [seed 1231244
          expected (rng/xoroshiro128+ seed)
          actual (lib/init-rng seed)]
      (is (= (rng/value expected) (rng/value @actual))))))

(deftest next-rng-test
  (testing "Updates the atom to the next value in the PRNG"
    (let [prng (lib/init-rng 1231231)
          expected (rng/next @prng)
          actual (lib/next-rng prng)]
      (is (and (= (rng/value expected) (rng/value actual))
               (= (rng/value expected) (rng/value @prng)))))))

(deftest get-double-test
  (testing "Gets the double value of the PRNG atom"
    (let [prng (lib/init-rng 1231231)
          expected (rng/long->unit-float (rng/value @prng))
          actual (lib/get-double prng)]
      (is (= expected actual)))))

(deftest random-letter-test
  (testing "Gets a random letter based off of the PRNG atom"
    (let [prng (lib/init-rng 1231231)
          expected-double (rng/long->unit-float (rng/value (rng/next @prng)))
          expected (nth lib/letters (* expected-double (count lib/letters)))
          actual (lib/random-letter prng)]
      (is (= expected actual)))))
