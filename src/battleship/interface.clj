(ns battleship.interface
  (:require [battleship.core :as c])
  (:require [battleship.supervisor :as s])
  (:require [clojure.set :as set]))

(def water "-")
(def ship "o")
(def shot "X")
(def fire "~")

(def water-board
  (into [] (repeat 10 (into [] (repeat 10 water)))))

(defn populate [board piece coords]
  (reduce #(assoc-in %1 %2 piece) board coords))

(defn create-board
  ([game player] (create-board game player false))
  ([game player fog?]
    (let [ships (-> game player :ships)
          shots (-> game player :shots)
          fires (set/intersection ships shots)
          board (-> water-board
                    (populate (#(if fog? water ship)) ships)
                    (populate shot shots)
                    (populate fire fires))]
      board)))

(defn new-game []
  (c/make-game))

(defn make-play [game player coord]
  (if (s/can-shoot? game player coord)
    (c/shoot game player coord)
    nil))

(defn place-ship [game player pos size direction]
  (if (s/can-place-ship? game player pos size direction)
    (c/place-ship game player pos size direction)
    nil))

(defn game-over? [game]
  (c/game-over? game))

(defn winner [game]
  (c/winner game))
