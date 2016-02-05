(ns hello-web.handler-spec
  (:require [speclj.core :refer :all]
            [ring.util.codec :as codec]
            [ring.mock.request :refer :all]
            [hello-web.handler :refer :all]))

(defn get-request [url]
  (app (request :get url)))

(describe "Testing the web app"
  (it "GET '/' should say Hello World."
      (let [response (get-request "/")]
        (should (.contains (:body response) "Hello World"))
        (should= 200 (:status response))))

  (it "GET '/invalid' should say Not Found"
      (let [response (get-request  "/invalid")]
        (should= 404 (:status response))))

  (it "GET '/users/new' should "
      (let [response (get-request  "/users/new")]
        (should= 200 (:status response))))

  (it "GET '/users' should should user's name "
      (let [response (get-request "/users/2?name=paul")]
        (should= 200 (:status response))
        (should (.contains (:body response) "paul"))))

  (it "GET '/users' should prevent cross site scripting attack"
      (let [response (get-request (str "/users/2?name=paul" (codec/url-encode "<script>alert('test');</script>")))]
        (should= 200 (:status response))
        (should (.contains (:body response) "paul&lt;script&gt;alert(&apos;test&apos;);&lt;/script&gt;"))))

  (it "POST '/users' should save user"
      (let [response (app (request :post "/users" {:name "paul"}))]
        (should= 302 (:status response))
        (should (.contains (get (:headers response) "Location") "/users")))))

(run-specs)