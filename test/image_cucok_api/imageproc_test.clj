(ns image-cucok-api.imageproc-test
  (:use [mikera.image.core])
  (:require [clojure.test :refer :all]
            [image-cucok-api.imageproc :refer :all]
            [clojure.repl :as repl])
  (:import java.awt.Image))

(defn invert-image-test [path]
  (invert-image (read-image path)))

(testing "invert-image"
  (testing "with invalid path"
    (is (thrown? Exception (invert-image-test "resources/public/test.jpg"))))
  (testing "with valid path"
    (is (instance? Image (invert-image-test "resources/public/example.jpg")))))

(testing "get-extension"
  (testing "with valid file name"
    (is (= "jpg" (get-extension "test.jpg")))))
  
(run-all-tests)
