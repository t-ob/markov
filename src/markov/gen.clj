(ns markov.gen
  (:require [clojure.string :as str]
            [markov.scrape :as scrape]))

(defn n-grams [n text]
  (partition n 1 (str/split text #"\s+")))

(defn next-term [p [[term q] & r]]
  (when q
    (if (and q (< p q))
      term
      (recur (- p q) r))))

(defn foo [text term]
  (let [unigram-frequencies (frequencies (n-grams 1 text))
        grouped-bigrams     (group-by first (n-grams 2 text))]
    (next-term (rand)
               (sort-by last
                        (for [[[_ next-term] y] (frequencies (get grouped-bigrams term)) :when y]
                          [next-term (double (/ y (get unigram-frequencies (list term))))])))))

(defn foo* [unigram-frequencies grouped-bigrams term]
  (next-term (rand)
             (sort-by last
                      (for [[[_ next-term] y] (frequencies (get grouped-bigrams term)) :when y]
                        [next-term (double (/ y (get unigram-frequencies (list term))))]))))

#_(def big-story (scrape/story "5951824"))

#_(def big-unigrams (n-grams 1 big-story))
#_(def big-bigrams  (n-grams 2 big-story))

#_(count big-unigrams)
#_(count big-bigrams)

#_(let [a (frequencies big-unigrams)
      b (group-by first big-bigrams)
      term "the"]
  (take 100 (iterate (partial foo* a b) term)))

#_(def a (frequencies big-unigrams))
#_(def b (group-by first big-bigrams))

(defn max-length-string [max-length [s & r]]
  (when (and s (<= (count s) max-length))
    (concat s (max-length-string (- max-length (count s)) r))))

(defn tweet [a b initial-term]
  (apply str (max-length-string (+ 80 (rand-int 60)) (interpose " " (iterate (partial foo* a b) initial-term)))))

#_(def hp-story (scrape/story "2290003"))
#_(def hp-unigrams (n-grams 1 hp-story))
#_(def hp-bigrams (n-grams 2 hp-story))

#_(def hp-a (frequencies hp-unigrams))
#_(def hp-b (group-by first hp-bigrams))

#_(println (tweet hp-a hp-b "Draco"))

#_(def lotr-story (scrape/story "644826"))
#_(def lotr-unigrams (n-grams 1 lotr-story))
#_(def lotr-bigrams (n-grams 2 lotr-story))

#_(def lotr-a (frequencies lotr-unigrams))
#_(def lotr-b (group-by first lotr-bigrams))

#_(def lotr-story-2 (scrape/story "1503169"))
#_(def lotr-unigrams-2 (n-grams 1 lotr-story-2))
#_(def lotr-bigrams-2 (n-grams 2 lotr-story-2))

#_(def lotr-a-2 (frequencies lotr-unigrams-2))
#_(def lotr-b-2 (group-by first lotr-bigrams-2))

#_(println (tweet lotr-a-2 lotr-b-2 "Boromir"))
