(ns hanafuda-cljs.vector)

(defn- sqr [v]
  (* v v))


(defn get-path [[x2 y2] [x1 y1]]
  [(- x2 x1)
   (- y2 y1)])


(defn dist [[x2 y2] [x1 y1]]
  (Math/sqrt
   (+ (sqr (- x2 x1))
      (sqr (- y2 y1)))))


(defn len [v]
  (dist v [0 0]))


(defn op [fn vec val]
  (mapv #(fn % val) vec))


(defn norm [v]
  (let [l (len v)]
    (op / v l)))


(defn add [v1 v2]
  (mapv + v1 v2))
