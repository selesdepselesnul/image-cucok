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

(defn process-image-with-val [params tempfile filename image-proc-func]
  (let [val (get (:params params) :value)]
          (process-image params tempfile filename (image-proc-func (read-string val)))))

(defn process-image-with-radius [params tempfile filename image-proc-func]
  (let [h-radius (get (:params params) :hradius)
        v-radius (get (:params params) :vradius)]
          (process-image params tempfile filename (image-proc-func (read-string h-radius) (read-string v-radius)))))

(defn image-post-req [route req-handler image-proc-func]
  (POST route
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (req-handler params tempfile filename image-proc-func)))

(defroutes api-routes
  
  (POST "/revert"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image params tempfile filename invert-image))

  (POST "/grayscale"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image params tempfile filename (grayscale)))
  
  (POST "/brightness"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image-with-val params tempfile filename brightness))

  (POST "/contrast"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image-with-val params tempfile filename contrast))

  (POST "/blur"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image params tempfile filename (blur)))
  
  (POST "/boxblur"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        (process-image-with-radius params tempfile filename box-blur))
  
  (image-post-req "/noise" process-image (noise))

  (route/resources "/"))

(def app
  (routes
    resouce-routes
    (-> api-routes
        (wrap-defaults site-defaults-setting) 
        (wrap-json-response))))
