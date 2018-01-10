(ns koi-koi-cljs.events
  (:require [re-frame.core :as re-frame]
            [koi-koi-cljs.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))
