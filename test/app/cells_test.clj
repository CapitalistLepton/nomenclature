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
(ns app.cells-test
  (:require [clojure.test :refer [deftest is testing]]
            [app.cells :as cells]))

(deftest make-entity-test
  (testing "Make a basic entity"
    (let [name [:A :1 :5]
          owner :testowner
          form :clay
          expected {:name name
                    :owner owner
                    :form form}]
      (is (= expected (cells/make-entity name owner form))))))

(deftest move-index-test
  (testing "Calculate new index correctly"
    (let [index 5
          dx 1
          dy 0
          size 5]
      (is (= 6 (cells/move-index index [dx dy] size)))))
  (testing "Move past max index"
    (let [index 20
          dx 0
          dy 1
          size 5]
      (is (= 0 (cells/move-index index [dx dy] size))))))

(deftest move-cells-test
  (testing "Move single entity correctly"
    (let [ent {:name [:2 :0 :0]
               :owner :player1
               :form :clay}
          cells [ent
                 nil
                 nil
                 nil]
          expected (list [1 ent]
                    [1 nil]
                    [2 nil]
                    [3 nil])]
      (is (= expected (cells/move-cells cells 2))))))

(deftest filter-collisions-test
  (testing "Group collided entities"
    (let [ent {:name [:2 :0 :0]
               :owner :player1
               :form :clay}
          indexed-cells (list [1 ent]
                              [1 nil]
                              [2 nil]
                              [3 nil])
          expected ['()
                    (list ent)
                    '()
                    '()]]
      (is (= expected (cells/filter-collisions indexed-cells 2))))))

(deftest handle-collisions-test
  (testing "Test initial logic"
    (let [cell-list [[1 2] [3]]
          expected '(1 3)]
      (is (= expected (cells/handle-collisions cell-list)))))
  (testing "nil with empty list"
    (is (= '(nil) (cells/handle-collisions [[]])))))
