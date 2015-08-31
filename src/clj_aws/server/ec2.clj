(ns clj-aws.server.ec2
  (:require [environ.core :refer [env]]
            [amazonica.core :refer [with-credential]]
            [clj-aws.server.asg :as asg :refer [cred]])
  (:use [amazonica.aws.ec2]))

(defn asg-reservations
  "It gets the instance information for each 
  instance that belongs to the asg.
  asg-info is a map: the result of calling (asg/asg-info-for asg-name)"
  [asg-info]
  (let [instances (flatten (vals asg-info))
        instance-names (map (fn [it] {:name (:instance it)}) instances)]
    (with-credential [(env :aws-key) (env :aws-secret) (env :aws-region)]
      (flatten (map (fn [it]
                      (-> (describe-instances :filters [{:name "instance-id"
                                                         :values [(:name it)]}])
                          :reservations))
                    instance-names)))))

(defn asg-instances
  "Retrieves the instances for an asg with the given name."
  [asg-name asgs]
  (let [asg-info (asg/asg-info-for asg-name asgs)]
    (->> (asg-reservations asg-info)
         (map (fn [it]
                (:instances it)))
         flatten)))

