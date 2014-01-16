(ns markov.mailonline.harvester
  (:require [markov.mailonline.common :refer [articles-base-dir screen-name-dir]]
            [markov.mailonline.articles :refer [article-urls]]
            [markov.mailonline.comments :refer [article-url->comments]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn create-screen-name-dir! [path]
  (.mkdir (io/file path)))

(defn harvest-comments! [limit mailonline-screen-name]
  (let [screen-name-dir (screen-name-dir mailonline-screen-name)]
    (create-screen-name-dir! screen-name-dir)
    (doseq [article-url (take limit (article-urls mailonline-screen-name))]
      (println article-url)
      (let [[_ _ _ article-id & _] (str/split article-url #"/")]
        (spit (format "%s/%s"
                      screen-name-dir
                      article-id)
              (try (str/join "\n" (article-url->comments article-url))
                   (catch Exception _ ""))))
      (Thread/sleep 1000))))

(def mailonline-screen-names
    [#_"MailOnline"
     "MailSport"
     "Femail"
     "DailyMailCeleb"])

#_(doseq [mailonline-screen-name mailonline-screen-names]
  (println (str "Harvesting " mailonline-screen-name " ..."))
    (harvest-comments! 300 mailonline-screen-name))
