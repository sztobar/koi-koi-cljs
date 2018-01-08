(ns hanafuda-cljs.actions
  (:require [hanafuda-cljs.vector :as v]
            [hanafuda-cljs.utils :as u]))


(defn move-to [obj dest h]
  (let [src (:pos obj)
        path (v/get-path dest src)
        dur (Math/floor (/ 500 u/timestep))
        delta-path (map #(/ % dur) path)
        norm-path (v/norm path)]
    (assoc obj
           :action {:type :move
                    :path path
                    :start-pos (:pos obj)
                    :duration dur
                    :t 0
                    :emit-after h})))


(defn give-p-hand [obj hand]
  (move-to obj (u/next-card-pos hand)
           :p-hand+))
