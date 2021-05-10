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
(ns app.rng-test
  (:require [clojure.test :refer [deftest is testing]]
            [app.rng :as rng]))

(deftest gen-random-test
  (testing "Make rng atom"
    (let [seed 1234
          prng (rng/gen-random seed)]
      (is (= seed @prng)))))

(deftest xor-shift-test
  (testing "Working XOR shift"
    (let [x 1
          expected 281612415666177]
      (is (= expected (rng/xor-shift x))))))

(deftest next!-test
  (testing "Working XOR shift PRNG"
    (let [seed 1234
          prng (rng/gen-random seed)
          expected 347502711545435295
          actual (rng/next! prng)]
      (is (= expected actual)))))

(deftest to-double-test
  (testing "Changes PRNG to be a double value"
    (let [seed 1234
          prng (rng/gen-random seed)
          expected 347502711545435295
          x (rng/next! prng)
          actual (rng/to-double x)]
      (is (= expected actual)))))
