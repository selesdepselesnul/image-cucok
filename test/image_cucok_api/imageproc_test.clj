(ns image-cucok-api.imageproc-test
  (:use [mikera.image.core])
  (:require [clojure.test :refer :all]
            [image-cucok-api.imageproc :refer :all]
            [mikera.image.core :refer :all]
            [clojure.repl :as repl]
            [mikera.image.filters :refer :all])
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

(testing "is-valid-extension?"
  (testing "with valid extension"
    (is (true? (is-valid-extension? "jpg"))))
  (testing "with invalid extension"
    (is (false? (is-valid-extension? "pdf")))))

(run-all-tests)
