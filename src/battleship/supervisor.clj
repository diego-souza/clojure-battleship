(ns battleship.supervisor
  (:require [battleship.vec :as vec])
  (:require [battleship.core :as c])
  (:require [clojure.set :refer :all]))

(defn inside-board? [[x y]]
  (and (>= x 0) (>= y 0) (<= x 9) (<= y 9)))

(defn can-place-ship? [game player pos size direction]
  (let [ships              (-> game player :ships)
        coords             (c/calculate-coords pos size direction)
        has-space?         (empty? (intersection ships coords))
        all-inside-board?  (empty? (remove inside-board? coords))
        can?               (and has-space? all-inside-board?)]
    can?))


(defn can-shoot? [game player pos]
  (let [shots              (-> game player :shots)
        new-target?        (not (contains? shots pos))
        current-player?    (= (game :current-player) player)
        can?               (and current-player? (inside-board? pos) new-target?)]
    can?))
