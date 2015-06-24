(ns battleship.repl
  (:require [battleship.interface :as i])
  (:require [battleship.generators :as gen])
  (:require [clojure.string :as s]))

(def border "  ")

(defn draw-game [game]
  (let [p1-board   (apply mapv vector (i/create-board game :player1 false)) 
        p2-board   (apply mapv vector (i/create-board game :player2 true))
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

(defn get-play [game player]
  (let [x (do (println "X:") (Integer/parseInt (read-line)))
        y (do (println "Y:") (Integer/parseInt (read-line)))
        new-game (i/make-play game player [x y])]
    (if new-game
      new-game
      (recur game player))))

(def current-game (atom (gen/generate-game)))

(defn -main [& args]
  (while (not (i/game-over? @current-game))
    (do
      (draw-game @current-game)
      (swap! current-game gen/generate-play :player2)
      (swap! current-game gen/generate-play :player1)
    ))
  (do
    (draw-game @current-game)
    (println "Winner: " (i/winner @current-game))
    ))
