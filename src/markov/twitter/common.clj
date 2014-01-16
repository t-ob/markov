(ns markov.twitter.common
  (:require [clojure.java.io :as io]
            [twitter.oauth :refer :all]
            [clj-time.format :as f]))

(defn oauth-creds* [{:keys [api-key api-secret oauth-token oauth-secret]}]
  (make-oauth-creds api-key api-secret oauth-token oauth-secret))

(def oauth-creds
  (memoize oauth-creds*))

(def app-oauth-creds
  (oauth-creds (read-string (slurp (io/resource "oauth-creds.edn")))))

(def twitter-formatter
  (f/formatter "E MMM d HH:mm:ss Z YYYY"))

(defn timestamp->date [timestamp]
  (f/parse twitter-formatter
           timestamp))
