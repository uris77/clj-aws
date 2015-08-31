(defproject clj-aws "1.0.0-SNAPSHOT"
  :description "Friendly display of ASGs details that makes sense. "
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/clojurescript "1.7.48"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0" :exclusions [org.eclipse.jetty/jetty-http 
                                                   org.eclipse.jetty/jetty-continuation]]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.1.18"]
                 [selmer "0.8.7"]
                 [amazonica "0.3.32"]
                 [re-frame "0.5.0-SNAPSHOT"]
                 [reagent "0.5.0"]
                 [reagent-utils "0.1.5"]
                 [cljs-http "0.1.37"]
                 [secretary "1.2.3"]]

  :jvm-opts ["-Xmx512m"]

  :plugins [[lein-environ "1.0.0"]]

  :min-lein-version "2.0.0"

  :main clj-aws.server.core

  :profiles {:dev-common   {:plugins       [[lein-cljsbuild "1.0.6"]
                                            [lein-figwheel "0.3.7"]]
                            :dependencies  [[reloaded.repl "0.1.0"]]
                            :env           {:dev? true}
                            :open-browser? true
                            :source-paths ["dev"]
                            :cljsbuild     {:builds [{:source-paths ["src/clj_aws/ui"]
                                                      :figwheel     true
                                                      :compiler     {:output-to "target/classes/public/js/app.js"
                                                                     :output-dir "target/classes/public/js/out"
                                                                     :asset-path "js/out"
                                                                     :optimizations :none
                                                                     :recompile-dependents true
                                                                     :main "clj-aws.ui.core"
                                                                     :source-map true}}]}}
             :dev-env-vars {}
             :dev [:dev-env-vars :dev-common]}

  )
