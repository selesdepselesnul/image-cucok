(ns image-cucok-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response resource-response content-type]]
            [image-cucok-api.imageproc :refer :all]
            [clojure.java.io :as io]))


(defroutes resouce-routes
  (GET "/images/:image"
       [image]
       (resource-response image {:root "public"})))

(defroutes api-routes
  (POST "/image_revert"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (let [file-extension (get-extension filename)
              returned-file
              (-> tempfile
                  read-image-by-file
                  invert-image
                  (write-image file-extension
                               (str "resources/public/image." file-extension)))]
          (response {:success returned-file})))
  (route/resources "/"))

(def app
  (routes
    resouce-routes
    (-> api-routes
        (wrap-defaults (assoc-in site-defaults
                                 [:security :anti-forgery]
                                 false))
        (wrap-json-response))))
