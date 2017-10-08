(ns image-cucok-api.imageproc
  (:use mikera.image.colours)
  (:use mikera.image.core)
  (:use mikera.image.filters)
  (:use mikera.image.spectrum)
  (:require [clojure.java.io :refer [resource file]])
  (:require [clojure.repl :as repl])
  (:require [mikera.image.filters :as filt])
  (:require [me.raynes.fs :as fs])
  (:import javax.imageio.ImageIO))

(defn read-image [path]
  (-> path file ImageIO/read))

;; (fs/extension "resources/example.jpg")
;; (ImageIO/write bi "png"  (File. "test.png"))

(defn invert-image [image]
  (filter-image image (filt/invert)))
