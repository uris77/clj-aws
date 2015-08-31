(ns clj-aws.server.routes
  (:require [compojure.core :refer [defroutes routes GET]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [environ.core :refer [env]]
            [selmer.parser :refer [render-file]]
            [clj-aws.server.asg :as asg]
            [clj-aws.server.ec2 :as ec2]))

;; extend cheshire to allow for the encoding of Joda DateTime values...

(extend-protocol cheshire.generate/JSONable
  org.joda.time.DateTime
  (to-json [t jg]
    (cheshire.generate/write-string jg (str t))))

(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello from AWS!"})

(defn render-index
  [req]
  (render-file "templates/index.html" {:dev {env :dev?}}))

(defn fetch-asgs
  [req]
  {:headers {"Content-Type" "application/json"}
   :body (asg/all-asgs)})

(defn fetch-ec2-instances
  [req]
  (let [asg-name (get-in req [:params :asg])
        instances (ec2/asg-instances asg-name (asg/all-asgs))]
    {:headers {"Content-Type" "application/json"}
     :body instances}))

(defroutes api-routes
  (GET "/api/asgs" _ fetch-asgs)
  (GET "/api/ec2" _ (wrap-json-body fetch-ec2-instances {:keyword? true})))

(defroutes site-routes
  (GET "/" [] render-index)
  (resources "/"))

(def app 
  (wrap-defaults (routes (-> api-routes
                             wrap-json-body
                             wrap-json-response) 
                         site-routes) 
                 (assoc-in site-defaults [:security :anti-forgery] false)))

