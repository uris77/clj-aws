(ns clj-aws.ui.misc)

(defn find-one
  "Finds an item in a collection.
  It returns the first item whose pred returns true.
  The predicate function receives the current item in the iteration."
  [pred coll]
  (apply (comp first filter) [pred coll]))

