(ns markov.mailonline.articles
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [markov.twitter.timeline :refer [timeline]]
            [clj-time.core :as t])
  (:import [java.net URI]))

(defn retweet? [tweet]
  (boolean (:retweeted_status tweet)))

(defn dailym-dot-ai-url? [url]
  (if url
    (= "dailym.ai" (.getHost (URI. url)))
    false))

(defn article-urls [mailonline-screen-name]
  (filter dailym-dot-ai-url?
          (map (comp :expanded_url first :urls :entities)
               (remove retweet?
                       (timeline mailonline-screen-name)))))
