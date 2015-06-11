(ns battleship.gui
  (:require [battleship.vec :as vec])
  (:require [battleship.core :as c])
  (:require [battleship.interface :refer :all])
  (:require [clojure.set :as set])
  (:require [clojure.string :as s]))

(use 'seesaw.core)

(native!)

(def f (frame :title "Battleship GUI"
              :width 800
              :height 600))

(defn display [c]
  (config! f :content c))

(defn create-label [text]
  (label :font "ARIAL-BOLD-40"
          :background :blue
          :text text))

(defn create-labels [player]
  (map #(create-label %) (flatten (create-board @current-game player))))

(defn create-grid [player]
  (grid-panel :id (str "grid-" (name player)) :columns 10 :rows 10 :items (create-labels player)))

(defn create-inputs [player]
  (grid-panel :columns 3 :rows 1 :items [(label (name player))
                                        (text :id (str "coord-x-" (name player)) :text "X")
                                        (text :id (str "coord-y-" (name player)) :text "Y")
                                        (button :id (str "shoot-" (name player)) :text "Shoot!")
                                        ]))

(defn create-split [player]
  (top-bottom-split (create-inputs player) (create-grid player)))

(def split (left-right-split  (create-split :player1) (create-split :player2) :divider-location 1/2))

(defn make-play [player]
  (let [x (Integer/parseInt (value (select f [(keyword (str "#coord-x-" (name player)))])))
        y (Integer/parseInt (value (select f [(keyword (str "#coord-y-" (name player)))])))]
  (do
    (swap! current-game c/shoot player [x y])
    (config! (select f [(keyword (str "#grid-" (name player)))]) :items (create-labels player)))))

@current-game

(listen (select f [:#shoot-player1]) :action (fn [e] (make-play :player1)))
(listen (select f [:#shoot-player2]) :action (fn [e] (make-play :player2)))


(display split)

(-> f
    ;pack!
    show!)
