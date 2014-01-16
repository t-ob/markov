(ns markov.guardian.harvester
  (:require [markov.guardian.common :refer [articles-base-dir screen-name-dir]]
            [markov.guardian.articles :refer [article-ids]]
            [markov.guardian.comments :refer [article-id->comments]]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn create-screen-name-dir! [path]
  (.mkdir (io/file path)))

(defn harvest-comments! [limit guardian-screen-name]
  (let [screen-name-dir (screen-name-dir guardian-screen-name)]
    (create-screen-name-dir! screen-name-dir)
    (doseq [article-id (take limit (article-ids guardian-screen-name))]
      (spit (format "%s/%s"
                    screen-name-dir
                    article-id)
            (try (str/join "\n" (article-id->comments article-id))
                 (catch Exception _ "")))
      (Thread/sleep 1000))))


(def guardian-twitter-screen-names
  ["gdnpolitics"
   "commentisfree"
   "guardianedu"
   "guardianeco"
   "societyguardian"])

#_(doseq [guardian-screen-name guardian-twitter-screen-names]
    (println (str "Harvesting " guardian-screen-name " ..."))
    (harvest-comments! 100 guardian-screen-name))



