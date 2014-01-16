(ns markov.mailonline.generate
  (:require [markov.gen :refer [n-grams next-term markov-text]]
            [markov.mailonline.common :refer [articles-base-dir screen-name-dir]]
            [markov.twitter.tweet :refer [tweet]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn comments-file->n-grams [n file]
  (mapcat (partial n-grams n)
          (str/split-lines (slurp file))))

(defn mailonline-screen-name->n-grams* [n mailonline-screen-name]
  (mapcat (partial comments-file->n-grams n)
          (rest (file-seq (io/file (screen-name-dir mailonline-screen-name))))))

(def mailonline-screen-name->n-grams
  (memoize mailonline-screen-name->n-grams*))

(defn sentence-starters* [unigrams]
  (let [capitals (set (map (comp char (partial + (int \A))) (range 26)))]
    (filter (fn [[f & _]]
              (contains? capitals f))
            (map first unigrams))))

(def sentence-starters
  (memoize sentence-starters*))

(defn make-tweet [mailonline-screen-name]
  (let [unigrams  (mailonline-screen-name->n-grams 1 mailonline-screen-name)
        bigrams   (mailonline-screen-name->n-grams 2 mailonline-screen-name)
        init-word (rand-nth (sentence-starters unigrams))]
    (tweet (markov-text unigrams bigrams init-word))))

