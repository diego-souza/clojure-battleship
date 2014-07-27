(ns battleship.core-test
  (:require [clojure.test :refer :all]
            [battleship.core :refer :all]))

(deftest making-a-game
  (testing "Initial state of the board"
    (let [g (make-game)
          initial-state {:ships #{} :shots #{}}]
      (is (= g {:player1 initial-state
                :player2 initial-state})))))

(deftest calculating-positions
  (testing "Calculation of positions based on starting position size and direction"
    (let [pos [1 1]
          size 2
          north (calculate-coords pos size :north)
          south (calculate-coords pos size :south)
          east  (calculate-coords pos size :east)
          west  (calculate-coords pos size :west)]
      (is (= north #{[1 0] [1 1]}))
      (is (= south #{[1 2] [1 1]}))
      (is (= east  #{[2 1] [1 1]}))
      (is (= west  #{[0 1] [1 1]})))))


(deftest placing-ships
  (testing "Placing ships on the board"
    (let [g (make-game)
          g-with-ship (place-ship g :player1 [1 1] 2 :north)]
      (is (= #{[1 1] [1 0]} (-> g-with-ship :player1 :ships))))))

(deftest shooting
  (testing "Shoting targets"
    (let [g (-> (make-game)
                (shoot :player1 [1 1]))]
      (is (= #{[1 1]} (-> g :player1 :shots))))))


(deftest game-over
  (testing "When game is over"
    (let [g (-> (make-game)
                (place-ship :player1 [0 0] 1 :north)
                (place-ship :player2 [0 0] 1 :north)
                (shoot      :player1 [0 0]))]
      (is (= (game-over? g) true))))
    (testing "When game is over"
    (let [g (-> (make-game)
                (place-ship :player1 [0 0] 1 :north)
                (place-ship :player2 [0 0] 1 :north))]
      (is (= (game-over? g) false)))))


(deftest asking-for-winner
  (testing "Who is the winning player - game over"
    (let [g (-> (make-game)
                (place-ship :player1 [0 0] 1 :north)
                (place-ship :player2 [0 0] 1 :north)
                (shoot      :player1 [0 0]))]
      (is (= (winner g) :player2))))
  (testing "Who is the winning player - game not over"
    (let [g (-> (make-game)
                (place-ship :player1 [0 0] 1 :north)
                (place-ship :player2 [0 0] 1 :north))]
      (is (= (winner g) nil)))))
