(ns markov.mailonline.common)

(def articles-base-dir
  (format "%s/mailonline"
          (System/getenv "HOME")))

(defn screen-name-dir [screen-name]
  (format "%s/%s"
          articles-base-dir
          screen-name))
