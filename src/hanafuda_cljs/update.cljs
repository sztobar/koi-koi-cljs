(ns hanafuda-cljs.update
  (:require [hanafuda-cljs.vector :as v]
            [hanafuda-cljs.events :refer [update-events]]
            [hanafuda-cljs.utils :as u]))


(defn pull-events [coll]
  (reduce
   (fn [{:keys [coll events] :as agg} obj]
     (if (contains? obj :emit)
       (let [obj' (dissoc obj :emit)
             event [(:emit obj) obj']]
         {:events (conj events event)
          :coll (conj coll obj')})
       {:events events
        :coll (conj coll obj)}))
   {:events []
    :coll []}
   coll))


(defn gather-card-events [state]
  (let [{:keys [coll events]} (pull-events (:cards state))]
    (assoc state
           :cards coll
           :events events)))



(defmulti update-action
  (fn [obj _] (get-in obj [:action :type])))


(defmethod update-action nil [obj _] obj)


(defn cubic-bezier [p0 p1 p2 p3 t]
  (+ (* (Math/pow (- 1 t) 3) p0)
     (* 3 (Math/pow (- 1 t) 2) t p1)
     (* 3 (- 1 t) (* t t) p2)
     (* (Math/pow t 3) p3)))

(defn sin-out-sine [t]
  (cubic-bezier 0.45 0.05 0.55 0.95 t))

(defmethod update-action :move [obj delta]
  (let [{:keys [action]} obj
        action' (update action :t inc)
        {:keys [t duration start-pos path]} action'
        delta-path (v/op * path (u/sine-easing (/ t duration)))
        pos' (v/add start-pos delta-path)]
    (if (= t duration)
      (assoc obj
             :pos pos'
             :action nil
             :emit (:emit-after action))
      (assoc obj
             :pos pos'
             :action action'))))


(defn update-cards [state delta]
  (-> state
      (update :cards
              #(map (fn [v] (update-action v delta)) %))
      (gather-card-events)))


(defn update-state [state delta]
  (-> state
      (update-cards delta)
      (update-events delta)))
