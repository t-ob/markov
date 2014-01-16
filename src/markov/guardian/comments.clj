(ns markov.guardian.comments
  (:require [markov.scrape :refer [url->hickory-response parse-text]]
            [markov.guardian.articles :refer [article-ids]]
            [hickory.select :as s]))

(defn article-id->discussion-url [article-id]
  (format "http://discussion.theguardian.com/discussion/p/%s"
          article-id))

(defn article-id->comments [article-id]
  (map (comp parse-text :content)
       (s/select (s/child (s/class :d2-body))
                 (url->hickory-response (article-id->discussion-url article-id)))))
