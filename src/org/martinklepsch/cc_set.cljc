(ns org.martinklepsch.cc-set
  #?(:clj (:require [clojure.string :as string]
                    [org.martinklepsch.cc_set.impl]))
  #?(:clj (:import [org.martinklepsch.cc_set.impl CustomComparatorSet]))
  #?(:cljs (:require [org.martinklepsch.cc-set.impl :refer [CustomComparatorSet]])))

(defn set-by
  "High level constructor for custom comparator sets.
   Throws if comparator returns nil."
  [comparator & keys]
  (let [cmp (fn [i] (if (nil? (comparator i))
                      (throw (ex-info "Custom set comparator returned nil" {:item i}))
                      (comparator i)))]
    (reduce conj (CustomComparatorSet. nil {} cmp) keys)))

#?(:clj (defmethod print-method CustomComparatorSet [v ^java.io.Writer w]
          (let [items (string/join " " (map pr-str (seq v)))]
            (.write w (str "#CustomComparatorSet{" items "}")))))

(comment
  (def x (CustomComparatorSet. {} :id))
  (conj x {:id 1} {:id 2})

  (set (seq x))

  (contains? (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})
  (disj (CustomComparatorSet. {"a" {:thing "a"}} :thing) {:thing "a"})

  (set-by :thing {:thing "a"} {:thing "x"} {:thing "b"})

  )
