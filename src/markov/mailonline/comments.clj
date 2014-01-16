(ns markov.mailonline.comments
  (:require [markov.scrape :refer [url->hickory-response parse-text]]
            [markov.mailonline.articles :refer [article-urls]]
            [hickory.select :as s]))

(defn article-url->comments [article-url]
  (map (comp parse-text :content)
       (s/select (s/child (s/class :comment-text))
                 (url->hickory-response article-url))))
