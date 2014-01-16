(ns markov.scrape
  (:require [clojure.zip :as zip]
            [clojure.string :as str]
            [clj-http.client :as http]
            [hickory.core :as hickory :refer [parse as-hickory]]
            [hickory.zip :refer [hickory-zip]]
            [hickory.select :as s]))

(defn url->hickory-response [url & {:as params}]
  (as-hickory (parse (:body (http/get url params)))))

(defn parse-text [story]
  (letfn [(parse-element [element]
            (cond
             (:content element) (parse-text (:content element))
             (string? element) element
             (coll? element) (clojure.string/join " " (map parse-text element))))]
    (str/replace (str/trim (str/join "\n"
                                     (map parse-element
                                          story)))
                 #"\s+"
                 " ")))

(def fanfiction-net-url
  "https://www.fanfiction.net")

;; Search

(defn fanfiction-net-search [fiction & {:keys [srt lan r s p] :or {srt 3 lan 1 r 10 s 2 p 1}}]
  (url->hickory-response (str fanfiction-net-url "/" fiction "/")
                         :query-params {:srt srt, :lan lan, :r r, :s s, :p p}))

#_(map (comp (partial re-find #"(?<=/s/)\d+") :href :attrs)
     (s/select (s/child (s/class :stitle))
               (fanfiction-net-search "game/Final-Fantasy-VII")))

#_(map (comp (partial re-find #"(?<=/s/)\d+") :href :attrs)
     (s/select (s/child (s/class :stitle))
               (fanfiction-net-search "book/Harry-Potter")))

#_(map (comp (partial re-find #"(?<=/s/)\d+") :href :attrs)
     (s/select (s/child (s/class :stitle))
               (fanfiction-net-search "book/Lord-of-the-Rings")))



;; Stories

(defn parse-storytext [story]
  (letfn [(parse-element [element]
            (cond
             (:content element) (parse-storytext (:content element))
             (string? element) element
             (coll? element) (clojure.string/join " " (map parse-storytext element))))]
    (str/join "\n"
              (map parse-element
                   story))))

(defn get-storytext [id chapter]
  (parse-storytext (:content (first (s/select (s/child (s/id :storytext))
                                              (url->hickory-response (str fanfiction-net-url "/s/" id "/" chapter)))))))

(defn story [id]
  (let [story-url (str fanfiction-net-url "/s/" id)
        hickory-response (url->hickory-response story-url)]
    (apply str
           (mapcat (comp (partial get-storytext id) :value :attrs)
                   (:content (first (s/select (s/id :chap_select) hickory-response)))))))

#_(story "9983730")

;; Guardian

#_(def guardian-test-response
  (url->hickory-response "http://www.theguardian.com/education/2014/jan/16/student-opportunity-fund-cut-decision"))

#_(def guardian-discussion
  (url->hickory-response "http://discussion.theguardian.com/discussion/p/3yqk7?commentpage=1&orderby=oldest&threads=collapsed&tab=picks&iframe=true&noposting=true"))


