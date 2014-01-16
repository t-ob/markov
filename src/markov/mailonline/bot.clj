(ns markov.guardian.bot
  (:require [markov.twitter.status :refer [update-status!]]
            [markov.mailonline.generate :refer [make-tweet]]))

(def mailonline-screen-names
    ["MailOnline"
     "MailSport"
     "Femail"
     "DailyMailCeleb"])

#_(loop []
    (let [message (make-tweet (rand-nth mailonline-screen-names))]
      (println (str "Tweeting: " message))
      (update-status! message)
      (Thread/sleep 30000)
      (recur)))





