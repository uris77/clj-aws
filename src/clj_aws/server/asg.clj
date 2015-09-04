(ns clj-aws.server.asg
  (:require [environ.core :refer [env]])
  (:use [amazonica.aws.autoscaling]))

(def cred {:access-key (env :aws-key)
           :secret-key (env :aws-secret)
           :endpoint   (env :aws-region)})

(defn- list-asgs
  []
  (let [instances (:auto-scaling-instances (describe-auto-scaling-instances cred))]
    (map (fn [instance] 
           {:instance (:instance-id instance)
            :availability-zone (:availability-zone instance)
            :health-status (:health-status instance)
            :group-name (:auto-scaling-group-name instance)})
         instances)))

(defn- remove-group-name-from-instance-list
  [coll]
  (map #(dissoc % :group-name) coll))

(defn all-asgs
  "Fetches all asgs and groups them by name, returning them in the format:
  [{:asg-name ({:instance instance-name :availability-zone zone :health-status STATUS})]"
  []
  (let [grouped-instances (group-by :group-name (list-asgs))]
    (reduce (fn [acc asg]
              (conj acc {:name (name (key asg)) :details (remove-group-name-from-instance-list (val asg))}))
            []
            grouped-instances)))

(defn asg-info-for
  [asg-name asgs]
  (first (filter #(= asg-name (:name %))
                 asgs)))

