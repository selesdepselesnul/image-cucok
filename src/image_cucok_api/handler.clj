(ns image-cucok-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [image-cucok-api.imageproc :refer :all]
            [clojure.java.io :as io]))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/image_revert"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (do
          (-> tempfile
              read-image-by-file
              invert-image
              (write-image "jpg" "resources/public/new_image.jpg"))
          (response {:success filename})))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-params
      wrap-keyword-params
      wrap-multipart-params
      wrap-json-response))
