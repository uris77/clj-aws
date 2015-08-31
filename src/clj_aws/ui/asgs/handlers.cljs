(ns clj-aws.ui.asgs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def app-db {:asgs []
             :vpcs []})

(defn fetch-asgs
  "Fetch all asgs from the server."
  []
  (go
    (let [resp (<! (http/get "/api/asgs" {"accept" "application/json"}))]
      (dispatch [:received-asgs (:body resp)]))))

(defn fetch-ec2-instances
  "Fetch ec2 instance for the given asg."
  [asg-name]
  (go
    (let [resp (<! (http/get (str "/api/ec2?asg=" asg-name) {"accept" "application/json"}))]
      (.log js/console "ec2: " (:body resp))
      (dispatch [:received-ec2-instances (:body resp)]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Handlers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(register-handler
 :initialize-db
 (fn [_ _]
   app-db))

(register-handler
 :get-asgs
 (fn [app-state _]
   (fetch-asgs)
   app-state))

(register-handler
 :received-asgs
 (fn [app-state [_ asgs]]
   (assoc app-state :asgs asgs)))

(register-handler
 :fetch-ec2-instances
 (fn [app-state [_ asg-name]]
   (fetch-ec2-instances asg-name)
   app-state))

(register-handler
 :received-ec2-instances
 (fn [app-state [_ ec2-instances]]
   (assoc app-state :vpcs ec2-instances)))


