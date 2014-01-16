(ns markov.guardian.bot
  (:require [markov.twitter.status :refer [update-status!]]
            [markov.guardian.generate :refer [make-tweet]]))

(def guardian-screen-names
  ["commentisfree"
   "societyguardian"
   "guardianedu"
   "gdnpolitics"
   "guardianeco"])

#_(loop []
    (let [message (make-tweet (rand-nth guardian-screen-names))]
      (println (str "Tweeting: " message))
      (update-status! message)
      (Thread/sleep 30000)
      (recur)))



