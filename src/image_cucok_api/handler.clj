(ns image-cucok-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response resource-response]]
            [image-cucok-api.imageproc :refer :all]
            [mikera.image.core :refer :all]
            [mikera.image.filters :refer :all]
            [clojure.java.io :as io]))

(defn now-str []
  (-> (java.time.LocalDateTime/now)
      (.format (java.time.format.DateTimeFormatter/ofPattern "yyyy_MM_dd_HH_mm_ss"))))

(defn generate-unique-name [file-extension]
  (str "img_" (now-str) "." file-extension))

(def site-defaults-setting
  (assoc-in site-defaults
            [:security :anti-forgery]
            false))

(defroutes resouce-routes
  (GET "/images/:image"
       [image]
       (resource-response image {:root "public"})))

(defn process-image [params tempfile filename image-proc-func]
  (if-let [file-extension (get-extension filename)]
    (if (is-valid-extension? file-extension)
      (let [new-file-name (generate-unique-name file-extension)
            returned-file
            (-> tempfile
                read-image-by-file
                image-proc-func
                (write-image file-extension
                             (str "resources/public/" new-file-name)))]
           (response {:url (str (:server-name params) ":" (:server-port params) "/images/" new-file-name)}))
     (response {:url ""}))
   (response {:url ""})))

(defroutes api-routes
  
  (POST "/revert"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image params tempfile filename invert-image))

  (POST "/brightness"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (let [val (get (:params params) :value)]
          (process-image params tempfile filename (brightness (read-string val)))))

  (POST "/grayscale"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image params tempfile filename (grayscale)))

  (route/resources "/"))

(def app
  (routes
    resouce-routes
    (-> api-routes
        (wrap-defaults site-defaults-setting) 
        (wrap-json-response))))
