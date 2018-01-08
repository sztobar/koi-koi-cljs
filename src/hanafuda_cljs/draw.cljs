(ns hanafuda-cljs.draw)


(def canvas-el (.getElementById js/document "canvas"))
(def canvas-w (.-width canvas-el))
(def canvas-h (.-height canvas-el))
(def ctx (.getContext canvas-el "2d"))
(def bg
  (let [gr (.createRadialGradient ctx 350 223 0 350 223 700)]
    (.addColorStop gr 0 "#fff")
    (.addColorStop gr 1 "#000")
    gr))

(def card-img
  (let [img (js/Image.)]
    (set! (.-src img) "cards.png")
    img))

(def card-w 56)
(def card-h 90)
(defn card-img-x [id]
  (* card-w
     (+ (mod id 4)
        (if (> id 23) 4 0))))
(defn card-img-y [id]
  (* card-h
     (mod (int (/ id 4))
          4)))

(defn draw-card [{:keys [data pos] :as card}]
  (.drawImage ctx
              card-img
              (card-img-x (:id data))
              (card-img-y (:id data))
              card-w
              card-h
              (first pos)
              (second pos)
              card-w
              card-h))


(defn draw-state [state]
  (.clearRect ctx 0 0 canvas-w canvas-h)
  (set! (.-fillStyle ctx) bg)
  (.fillRect ctx 0 0 700 700)
  (mapv draw-card (:cards state)))
