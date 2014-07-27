(ns battleship.core
  (:require [battleship.vec :as vec])
  (:require [clojure.set :refer :all]))

(defn make-game []
  (let [initial-state {:ships #{} :shots #{}}]
    {:player1 initial-state
     :player2 initial-state}))

(defn delta [direction]
  (case direction
    :north [0 -1]
    :south [0 1]
    :east  [1 0]
    :west  [-1 0]))


; Change Later
;(set (take 4 (iterate #(vec (vec/add % (delta :south))) [1 1])))
(defn calculate-coords [pos size direction]
  (let [deltas (map #(vec/scale % (delta direction)) (range size))]
    (set (map vec (map #(vec/add % pos) deltas)))))

(defn place-ship [game player pos size direction]
  (let [ships       (-> game player :ships)
        coords      (calculate-coords pos size direction)
        new-ships   (apply conj ships coords)
        new-game    (assoc-in game [player :ships] new-ships)]
    new-game))

(defn shoot [game player pos]
   (let [shots      (-> game player :shots)
         new-shots  (conj shots pos)
         new-game   (assoc-in game [player :shots] new-shots)]
     new-game))

(defn player-dead? [g player]
  (let [ships (-> g player :ships)
        shots (-> g player :shots)]
    (subset? ships shots)))

(defn game-over? [g]
  (let [p1-dead? (player-dead? g :player1)
        p2-dead? (player-dead? g :player2)]
      (or p1-dead? p2-dead?)))

(defn winner [g]
  (when (game-over? g)
    (if (player-dead? g :player1) :player2 :player1)))















