(ns battleship.supervisor-test
  (:require [clojure.test :refer :all]
            [battleship.core :as c]
            [battleship.supervisor :refer :all]))

(deftest possibility-of-placing-ships
  (testing "Placing one ship next to other"
    (let [g (-> (c/make-game)
                (c/place-ship :player1 [1 1] 1 :north))]
      (is (= true (can-place-ship? g :player1 [2 2] 1 :north)))))

  (testing "Placing one ship above other"
    (let [g (-> (c/make-game)
                (c/place-ship :player1 [1 1] 1 :north))]
      (is (= false (can-place-ship? g :player1 [1 1] 1 :north)))))


  (testing "Placing ship inside the board"
    (let [g (c/make-game)]
      (are [coord]
           (= true (can-place-ship? g :player1 coord 1 :north))
           [0 0]
           [1 1]
           [4 5]
           [9 9])))

  (testing "Placing ship outside the board"
    (let [g (c/make-game)]
      (are [coord]
           (= false (can-place-ship? g :player1 coord 1 :north))
           [0 -1]
           [-1 0]
           [-1 -1]
           [10 4]
           [4 10]
           [10 10]))))

(deftest possibility-of-shooting
  (testing "shooting next to previous shot"
    (let [g (-> (c/make-game)
                (c/shoot :player1 [1 1]))]
      (are [coord]
           (= true (can-shoot? g :player1 coord))
           [0 0]
           [1 0]
           [2 0]
           [0 1]
           [2 1]
           [0 2]
           [1 2]
           [2 2])))

  (testing "shooting in the same place"
    (let [g (-> (c/make-game)
                (c/shoot :player1 [0 0]))]
      (is (= false (can-shoot? g :player1 [0 0])))))

  (testing "Shooting inside the board"
    (let [g (c/make-game)]
      (are [coord]
           (= true (can-shoot? g :player1 coord))
           [0 0]
           [1 1]
           [4 5]
           [9 9])))

  (testing "Shooting outside the board"
    (let [g (c/make-game)]
      (are [coord]
           (= false (can-shoot? g :player1 coord))
           [0 -1]
           [-1 0]
           [-1 -1]
           [10 4]
           [4 10]
           [10 10])))

  (testing "shooting without being current-player"
    (let [g (c/make-game)]
      (is (= false (can-shoot? g :player2 [0 0]))))))
