(ns hanafuda-cljs.events)


(defmulti apply-event (fn [_ [event _]] event))

(defmethod apply-event :p-hand+ [st [_ card]]
  (let [{:keys [cards p-hand]} st
        card-id (get-in card [:data :id])]
    (assoc st
           :cards (filter #(= card-id (get-in % [:data :id])) cards)
           :p-hand (update p-hand :cards conj (:data card)))))


(defmethod apply-event :log [st _]
  (println "object stopped moving")
  st)


(defmethod apply-event :default [st _] st)


(defn update-events [{:keys [events] :as st} _]
  (reduce apply-event
          (dissoc st :events)
          events))
