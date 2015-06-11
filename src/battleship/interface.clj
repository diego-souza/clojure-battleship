(ns battleship.interface
  (:require [battleship.vec :as vec])
  (:require [battleship.core :as c])
  (:require [battleship.supervisor :refer :all])
  (:require [clojure.set :as set])
  (:require [clojure.string :as s]))

(def water "-")
(def ship "o")
(def shot "X")
(def fire "~")
(def border "  ")

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
