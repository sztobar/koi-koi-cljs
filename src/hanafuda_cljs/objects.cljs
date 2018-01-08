(ns hanafuda-cljs.objects
  (:require [hanafuda-cljs.utils :as u]))


(def init-obj {:pos [0 0]})


(defn object [data]
  (merge init-obj data))


(defn card [v]
  (object {:data v
           :pos u/deck-pos}))
