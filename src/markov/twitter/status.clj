(ns markov.twitter.status
  (:require [markov.twitter.common :refer [app-oauth-creds twitter-formatter timestamp->date]]
            [twitter.api.restful :as rest]))

(defn update-status! [message]
  (rest/statuses-update :oauth-creds app-oauth-creds
                        :params      {:status message}))
