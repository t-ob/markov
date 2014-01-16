(ns markov.guardian.articles
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [markov.twitter.timeline :refer [timeline]]
            [clj-time.core :as t])
  (:import [java.net URI]))

(defn retweet? [tweet]
  (boolean (:retweeted_status tweet)))

(defn gu-dot-com-url? [url]
  (if url
    (= "gu.com" (.getHost (URI. url)))
    false))

(defn gu-dot-com-url->article-id [guardian-url]
  (let [[_ _ _ _ id _ & _] (str/split guardian-url #"/")]
    id))

(defn article-ids [guardian-twitter-screen-name]
  (map gu-dot-com-url->article-id
       (filter gu-dot-com-url?
               (map (comp :expanded_url first :urls :entities)
                    (remove retweet?
                            (timeline guardian-twitter-screen-name))))))
