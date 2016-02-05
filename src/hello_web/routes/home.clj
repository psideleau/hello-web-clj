(ns hello-web.routes.home
  (:require [compojure.core :refer [GET POST defroutes]]
            [ring.util.response :as response]
            [hello-web.views.layout :as layout]
            [hiccup.form :as form]
            [hiccup.util :as util]
            [hiccup.element :as elm]))

(defn home []
  (layout/common [:h1 "Hello World!"](elm/link-to "users/new" "Create new user")))

(defn new-user []
  (layout/common
    [:h1 "New User"]
      (form/form-to [:post "/users"]
        (form/label "name" "Enter name")
        (form/text-field "name")
        (form/submit-button "Submit"))))

(defn create-user! [req]
  (response/redirect (str "/users/1?name=" (get (:params req) :name))))

(defn get-user [req]
  (layout/common [:h1 (str "User: " (util/escape-html (get (:params req) :name)))]))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/users/new" req (new-user))
  (GET "/users/:id" req (get-user req))
  (POST "/users"    req (create-user! req)))