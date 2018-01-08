(ns hanafuda-cljs.utils)


(def timestep (/ 1000 60))

(def card-width 96)

(def hand-padding 10)

(def deck-pos [50 200])


(defn next-card-pos [cont]
  (-> (+ card-width hand-padding)
      (* (count (:cards cont)))
      (#(let [[x y] (:pos cont)]
         [(+ % x) y]))))

(defn sine-easing [t]
  (Math/sin (* t (/ Math/PI 2))))


