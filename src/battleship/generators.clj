(ns battleship.generators
  (:require [battleship.vec :as vec])
  (:require [battleship.core :as c])
  (:require [battleship.supervisor :refer :all])
  (:require [clojure.set :as set])
  (:require [clojure.string :as s]))

(defn generate-play [game player]
  (let [x (rand-int 10)
        y (rand-int 10)]
  (if
    (can-shoot? game player [x y])
    (c/shoot game player [x y])
    (generate-play game player))))

(defn generate-ship [game player size]
  (let [x (rand-int 10)
        y (rand-int 10)
        d (rand-nth [:north :south :east :west])]
  (if
    (can-place-ship? game player [x y] size d)
    (c/place-ship game player [x y] size d)
    (generate-ship game player size))))

(defn generate-ships [game player sizes]
  (reduce #(generate-ship %1 player %2) game sizes))

(defn generate-game []
  (-> (c/make-game)
      (generate-ships :player1 [1 1 2 2 3 4 5])
      (generate-ships :player2 [1 1 2 2 3 4 5])))

(defn get-play [game player]
  (let [x (do (println "X:") (Integer/parseInt (read-line)))
        y (do (println "Y:") (Integer/parseInt (read-line)))]
    (if (can-shoot? game player [x y])
      (c/shoot game player [x y])
      (get-play game player))))
