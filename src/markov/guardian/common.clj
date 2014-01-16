(ns markov.guardian.common)

(def articles-base-dir
  (format "%s/guardian_comments"
          (System/getenv "HOME")))

(defn screen-name-dir [screen-name]
  (format "%s/%s"
          articles-base-dir
          screen-name))
