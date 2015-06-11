(ns battleship.repl
  (:require [battleship.vec :as vec])
  (:require [battleship.core :as c])
  (:require [battleship.interface :refer :all])
  (:require [battleship.generators :refer :all])
  (:require [clojure.set :as set])
  (:require [clojure.string :as s]))


(defn draw-game [game]
  (let [p1-board   (apply mapv vector (create-board game :player1 false)) 
        p2-board   (apply mapv vector (create-board game :player2 true))
        p1-string  (map #(s/join border %) p1-board)
        p2-string  (map #(s/join border %) p2-board)
        p1-ycoords (map str (map #(str % "  ") (range 10)) p1-string)
        p2-ycoords (map str (map #(str % "  ") (range 10)) p2-string)
        p1-xcoords (cons "   0  1  2  3  4  5  6  7  8  9" p1-ycoords)
        p2-xcoords (cons "   0  1  2  3  4  5  6  7  8  9" p2-ycoords)
        p1-name    (cons "   Player 1                    " p1-xcoords)
        p2-name    (cons "   Player 2                    " p2-xcoords)]
    (do
      (println (apply str (map #(str %1 "          " %2 "\n") p1-name p2-name)))
     )))

(def current-game (atom (generate-game)))

(defn -main [& args]
  (while (not (c/game-over? @current-game))
    (do
      (draw-game @current-game)
      (swap! current-game get-play :player2)
      (swap! current-game generate-play :player1)
    ))
  (do
    (draw-game @current-game)
    (println "Winner: " (c/winner @current-game))
    ))
