(ns image-cucok-api.imageproc
  (:use mikera.image.colours)
  (:use mikera.image.core)
  (:use mikera.image.filters)
  (:use mikera.image.spectrum)
  (:require [clojure.java.io :refer [resource file]])
  (:require [clojure.repl :as repl])
  (:require [mikera.image.filters :as filt])
  (:require [clojure.string :as str])
  (:require [me.raynes.fs :as fs])
  (:import java.io.File)
  (:import javax.imageio.ImageIO))

(defn read-image [path]
  (-> path file ImageIO/read))

(defn read-image-by-file [file]
  (ImageIO/read file))

(defn get-extension [path]
  (-> path
      fs/extension
      (str/split #"\.")
      (nth 1 nil)))

(defn write-image [image ext dest]
  (let [status
        (ImageIO/write image
                 ext
                 (File. dest))]
    (if status dest nil)))

(defn invert-image [image]
  (filter-image image (filt/invert)))

(defn is-valid-extension? [extension]
  (contains? #{"jpg" "jpeg" "png" "bmp"} extension))

