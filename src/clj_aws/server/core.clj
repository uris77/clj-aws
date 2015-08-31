(ns clj-aws.server.core
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]
            [clj-aws.server.routes :refer [app]]))

(defn- start-server [handler port]
  (let [server (run-server handler {:port port})]
    (println (str "Started server on localhost:" port))
    server))

(defn- stop-server [server]
  (when server
    (server))) ;; run-server returns a fn that stops itself

(defrecord AwsAsgPprint []
  component/Lifecycle
  (start [this]
    (assoc this :server (start-server #'app 9009)))
  (stop [this]
    (stop-server (:server this))
    (dissoc this :server)))

(defn create-system []
  (AwsAsgPprint.))


(defn -main
  [& args]
  (.start (create-system)))
