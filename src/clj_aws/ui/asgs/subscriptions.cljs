(ns clj-aws.ui.asgs.subscriptions
  (:require [re-frame.core :refer [register-sub]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(register-sub
 :asgs
 (fn [app-state _]
   (reaction (:asgs @app-state))))

(register-sub
 :vpcs
 (fn [app-state _]
   (reaction (:vpcs @app-state))))
