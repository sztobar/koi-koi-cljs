(ns hanafuda-cljs.core
  (:require
   [hanafuda-cljs.deck :refer [create-deck cards]]
   [hanafuda-cljs.update :refer [update-state]]
   [hanafuda-cljs.draw :refer [draw-state]]
   [hanafuda-cljs.utils :as u]
   [hanafuda-cljs.actions :as a]
   [hanafuda-cljs.objects :as obj]
   [cljs.core.async :refer [<! chan sliding-buffer put! close! timeout]])
  (:require-macros
   [cljs.core.async.macros :refer [go-loop go]]))

(enable-console-print!)


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


(def init-state {:deck (create-deck)
                 :p-hand []
                 :p-field []
                 :o-hand []
                 :o-field []
                 :field []
                 :cards [(obj/card (get cards 0))]
                 :events []
                 :running false})


(defonce app-state (atom init-state))


(defn reset-state [_ cur-time]
  (-> init-state
      (assoc :running true)
      #_(give-p-deck-card)))


(defn time-loop [timestamp]
  (let [curr-state @app-state
        {:keys [frame-delta last-frame-time-ms]} curr-state
        frame-delta (+ frame-delta (- timestamp last-frame-time-ms))
        last-frame-time-ms timestamp]
    (loop [delta frame-delta]
      (if (>= delta u/timestep)
        (do
          (swap! app-state update-state u/timestep)
          (recur (- delta u/timestep)))
        (swap! app-state
               #(assoc % :frame-delta delta
                         :last-frame-time-ms last-frame-time-ms))))
    (draw-state @app-state)
    (.requestAnimationFrame js/window time-loop)))


(defn start-game []
  (println "start-game 222")
  (.requestAnimationFrame
   js/window
   (fn [time]
     (reset! app-state (reset-state @app-state time))
     (time-loop time))))


(defn give-p-deck-card [{:keys [deck cards p-hand] :as st}]
  (let [[card & rest] deck]
    (assoc st
     :deck rest
     :cards (conj cards
             (-> (obj/card card)
                 (a/give-p-hand p-hand))))))

(when-not (:running @app-state)
  (start-game))


(.addEventListener (.getElementById js/document "canvas")
                   "click"
                   (fn [ev]
                     (swap! app-state (fn [st]
                                        (assoc st :cards [(a/move-to (get-in st [:cards 0])
                                                                     [(.-layerX ev) (.-layerY ev)]
                                                                     :log)])))))

; (defn deal-cards [state ch]
;   (let [out-ch (chan)]
;     (go (-> (deal-p-hand)
;             (deal-o-hand)
;             (deal-field))
;     out-ch))
