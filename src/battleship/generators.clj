(ns battleship.generators
  (:require [battleship.interface :as i]))

(defn generate-play [game player]
  (let [x (rand-int 10)
        y (rand-int 10)
        new-game (i/make-play game player [x y])]
  (if new-game
    new-game
    (recur game player))))

(defn generate-ship [game player size]
  (let [x (rand-int 10)
        y (rand-int 10)
        d (rand-nth [:north :south :east :west])
        new-game (i/place-ship game player [x y] size d)]
  (if new-game
    new-game
    (recur game player size))))

(defn generate-ships [game player sizes]
  (reduce #(generate-ship %1 player %2) game sizes))

(defn generate-game []
  (-> (i/new-game)
      (generate-ships :player1 [1 1 2 2 3 4 5])
      (generate-ships :player2 [1 1 2 2 3 4 5])))
