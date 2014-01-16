(ns markov.gen
  (:require [clojure.string :as str]
            [markov.scrape :as scrape]))

(defn n-grams [n text]
  (partition n 1 (str/split text #"\s+")))

(defn unigram-frequencies* [unigrams]
  (frequencies unigrams))

(def unigram-frequencies
  (memoize unigram-frequencies*))

(defn grouped-bigrams* [bigrams]
  (group-by first bigrams))

(def grouped-bigrams
  (memoize grouped-bigrams*))

(defn next-term [unigrams bigrams term]
  (letfn [(choose-at [p [[t q] & r]]
            (when q
              (if (and q (< p q))
                t
                (recur (- p q) r))))]
    (choose-at (rand)
               (sort-by last
                        (for [[[_ next] y] (frequencies (get (grouped-bigrams bigrams) term)) :when y]
                          [next (double (/ y (get (unigram-frequencies unigrams) (list term))))])))))

(defn markov-text [unigrams bigrams initial-term]
  (iterate (partial next-term unigrams bigrams) initial-term))
