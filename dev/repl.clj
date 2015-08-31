(ns repl
 (:require [reloaded.repl :refer [system reset stop]]
            [clj-aws.server.core]))

(reloaded.repl/set-init! #'clj-aws.server.core/create-system)

