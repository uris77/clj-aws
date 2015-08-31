(ns clj-aws.ui.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [clj-aws.ui.asgs.views :refer [main-panel]]
            [clj-aws.ui.asgs.subscriptions]
            [clj-aws.ui.asgs.handlers]
            [clj-aws.ui.routes :as routes]))

(defn mount-root
  []
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init
  []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

(init)

