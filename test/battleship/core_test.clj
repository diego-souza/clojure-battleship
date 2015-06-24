(ns battleship.core-test
  (:require [clojure.test :refer :all]
            [battleship.core :refer :all]))

(deftest making-a-game
  (testing "Initial state of the board"
    (let [g (make-game)
          initial-state {:ships #{} :shots #{}}]
      (is (= g {:player1 initial-state
                :player2 initial-state
                :current-player :player1})))))

(deftest calculating-positions
  (testing "Calculation of positions based on starting position size and direction"
    (are [direction, coords]
         (= (calculate-coords [1 1] 2 direction) coords)
         :north #{[1 0] [1 1]}
         :south #{[1 2] [1 1]}
         :east  #{[2 1] [1 1]}
         :west  #{[0 1] [1 1]})))

(deftest placing-ships
  (testing "Placing ships on the board"
    (let [g (-> (make-game)
                (place-ship :player1 [1 1] 2 :north))]
      (is (= #{[1 1] [1 0]} (-> g :player1 :ships))))))

(deftest shooting
  (testing "Shoting targets"
    (let [g (-> (make-game)
                (shoot :player1 [1 1]))]
      (is (= #{[1 1]} (-> g :player1 :shots))))))

(deftest ending-turn
  (testing "Ending turn"
    (let [g (-> (make-game)
                (end-turn))]
      (is (= :player2 (g :current-player))))))

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
