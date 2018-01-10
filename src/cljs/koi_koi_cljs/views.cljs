(ns koi-koi-cljs.views
  (:require [re-frame.core :as re-frame]
            [koi-koi-cljs.subs :as subs]
            ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div "Hello from " @name]))
