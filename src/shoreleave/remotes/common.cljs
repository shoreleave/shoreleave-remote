(ns shoreleave.remotes.common
  "Common remote operations for packaging up calls"
  (:require [clojure.string :as cstr]
            [goog.Uri.QueryData :as query-data]
            [goog.structs :as structs]
            [goog.string :as gstr]
            [shoreleave.browser.cookies :as cookies]))

;; ###Attention:
;; These are intended for internal use only.  You should not use these directly.

(def event-types
  {:on-complete goog.net.EventType.COMPLETE
   :on-success goog.net.EventType.SUCCESS
   :on-error goog.net.EventType.ERROR
   :on-timeout goog.net.EventType.TIMEOUT
   :on-ready goog.net.EventType.READY})

(defn rand-id-str
  "Generate a random string that is suitable for request IDs"
  []
  (gstr/getRandomString))

(defn ->url-method
  "Given the keyword form of a request method (`:post`),
  return Closure acceptable form (an upper-cased string)"
  [m]
  (cstr/upper-case (name m)))

(defn parse-route
  "Shape the routes accordingly for Closure's XHR calls"
  [route]
  (cond
    (string? route) ["GET" route]
    (vector? route) (let [[m u] route]
                      [(->url-method m) u])
    :else ["GET" route]))

(defn ->simple-callback
  "Liberate all client-side developers!
  Given a simple callback function, automatically pass it the response
  content from a remote call"
  [callback]
  (when callback
    (fn [req]
      (let [data (.getResponseText req)]
        (callback data)))))

(defn csrf-protected
  "For all POST requests, if ring-anti-forgery is used, pack the CSRF token
  into the content being sent to the server.
  Content is always sent to the server as a map (that later gets converted accordingly)"
  [content-map method]
  (if-let [anti-forgery-token (and (= method "POST")
                                   (:__anti-forgery-token cookies/cookies))]
    (merge content-map {:__anti-forgery-token anti-forgery-token})
    content-map))

(defn ->data-str
  "Generate a query-data-string, given Clojure data (usually a hash-map)"
  [d]
  (let [cur (clj->js d)
        query (query-data/createFromMap (structs/Map. cur))]
    (str query)))
 
