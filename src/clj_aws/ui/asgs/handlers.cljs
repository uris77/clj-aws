(ns clj-aws.ui.asgs.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [clj-aws.ui.misc :refer [find-one]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def app-db {:asgs []
             :vpcs {}
             :selected-asg ""
             :loading-vpcs? false
             :loading-asgs? true})

(defn fetch-asgs
  "Fetch all asgs from the server."
  []
  (go
    (let [resp (<! (http/get "/api/asgs" {"accept" "application/json"}))]
      (dispatch [:received-asgs (:body resp)]))))

(defn request-ec2-instances
  "Requests the ec2 instances from the server for the given asg."
  [asg-name]
  (go
    (let [resp (<! (http/get (str "/api/ec2?asg=" asg-name) {"accept" "application/json"}))]
      (dispatch [:received-ec2-instances {:asg asg-name :vpcs (:body resp)}]))))

(defn fetch-ec2-instances
  [asg-name asgs]
  (let [cached-vpcs (find-one #(= asg-name (:name %)) asgs)]
    (if (empty? (:vpcs cached-vpcs))
      (request-ec2-instances asg-name)
      (dispatch [:received-ec2-instances {:asg asg-name :vpcs (:vpcs cached-vpcs)}]))))

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
   (-> app-state
       (assoc :loading-asgs? false)
       (assoc :asgs asgs))))

(register-handler
 :fetch-ec2-instances
 (fn [app-state [_ asg-name]]
   (if (not= asg-name (get-in app-state [:vpcs :asg]))
     (do
       (fetch-ec2-instances asg-name (:asgs app-state))
       (assoc app-state :loading-vpcs? true))
     app-state)))

(register-handler
 :received-ec2-instances
 (fn [app-state [_ ec2-instances]]
   (let [asg (find-one #(= (:asg ec2-instances) (:name %)) (:asgs app-state))
         new-asgs (reduce (fn [acc it]
                            (if (= (:name it) (:asg ec2-instances))
                               (conj acc (assoc it :vpcs (:vpcs ec2-instances)))
                               (conj acc it)))
                          []
                          (:asgs app-state)) ]
     (-> (assoc app-state :asgs new-asgs)
         (assoc :loading-vpcs? false)
         (assoc :vpcs ec2-instances)))))


