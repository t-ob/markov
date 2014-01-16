(ns markov.twitter.timeline
  (:require [markov.twitter.common :refer [app-oauth-creds twitter-formatter timestamp->date]]
            [twitter.api.restful :as rest]
            [clj-time.core :as t]))

(defn timeline-page
  [creds screen-name & {:keys [max-id since until]}]
  (let [params (merge {:screen-name screen-name
                       :count       200}
                      (when max-id
                        {:max-id max-id}))]
    (:body (rest/statuses-user-timeline :oauth-creds creds
                                        :params      params))))

(defn timeline*
  [creds screen-name & {:keys [max-id]}]
  (when-let [results (seq (timeline-page creds screen-name :max-id max-id))]
    (lazy-cat results
              (timeline* creds
                         screen-name
                         :max-id (dec (:id (last results)))))))

(defn timeline
  [screen-name & {:keys [since until]
                  :or   {since (t/epoch)
                         until (t/now)}}]
  (let [tweet->date (comp timestamp->date :created_at)]
    (take-while (comp (partial t/before? since) tweet->date)
                (drop-while (comp (partial t/before? until) tweet->date)
                            (timeline* app-oauth-creds screen-name)))))
