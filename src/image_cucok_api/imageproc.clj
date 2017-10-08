(ns image-cucok-api.imageproc
  (:use mikera.image.colours)
  (:use mikera.image.core)
  (:use mikera.image.filters)
  (:use mikera.image.spectrum)
  (:require [clojure.java.io :refer [resource file]])
  (:require [clojure.repl :as repl])
  (:require [mikera.image.filters :as filt])
  (:import javax.imageio.ImageIO))

(defn read-image [path]
  (-> path file ImageIO/read))

(defn invert-image [image]
  (filter-image image (filt/invert)))
