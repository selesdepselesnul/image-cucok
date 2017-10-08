(ns image-cucok-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/image_revert" [param] (response {:foo "bar"}))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-params
      wrap-multipart-params
      wrap-json-response))
