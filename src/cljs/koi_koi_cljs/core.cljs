(ns koi-koi-cljs.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [koi-koi-cljs.events :as events]
            [koi-koi-cljs.views :as views]
            [koi-koi-cljs.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
