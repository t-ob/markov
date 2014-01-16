(ns markov.twitter.tweet
  (:require [clojure.string :as str]))

;; (max-length-string 10 "Hello")

(defn max-length-string [max-length strings]
  (letfn [(foo [max-length [s & r]]
            (when (and s (<= (count s) max-length))
              (concat s (foo (- max-length (count s)) r))))]
    (str/trim (apply str
                     (foo max-length
                          (interpose " " strings))))))

(defn tweet [strings]
  (max-length-string (+ 50 (rand-int 90))
                     strings))
