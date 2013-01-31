(ns shoreleave.remotes.xhr
  "Shoreleave's XmlHttpRequest"
  (:require [goog.net.XhrIo :as xhr]
            [goog.events :as events]
            [shoreleave.remotes.common :as common]))

;; XMLHttpRequests - `xhr`
;; -----------------------
;; You are encouraged to use the xhr pool via the request call.
;;
;; If you are in a situation where you need a hand-crafted one-off xhr call,
;; use this function - a wrapper around Closure's [XhrIo object](http://closure-library.googlecode.com/svn/docs/class_goog_net_XhrIo.html)
;;
;; The `(xhr ...)` function takes a mandatory `route` argument, in the format [:method URL-str] -> [:get "/fetch-recent-results"]
;; URLs/Routes can also be expressed as `\"http://www.google.com\"` with a default GET method added
;;
;; By default, if you don't specify an `:on-error` handler, errors will be logged to the console
;;
;; Additional options are passed in as key'd args:
;;
;;  * :on-success `(fn [result] (js/console.log "This is a callback function for successful requests"))`
;;  * :on-error `(fn [result] (js/console.log "We failed."))`
;;  * :content `{:one-arg "Sending this to the server" :another 5}`
;;  * :headers `{}` - additional header information
;;
;;  If you need error handling, you must use `(request ...)`

(defn xhr [route & opts]
  (let [req (goog.net.XhrIo.)
        [method uri] (common/parse-route route)
        {:keys [on-success on-error content headers]} (apply hash-map opts)
        content (common/csrf-protected content method)
        data (common/->data-str content)
        suc-callback (common/->simple-callback on-success)
        err-callback (common/->simple-callback (or on-error #(js/console.log (str "XHR ERROR: " %))))]
    (when suc-callback
      (events/listen req (common/event-types :on-success) #(suc-callback req))
      (events/listen req (common/event-types :on-error) #(err-callback req)))
    (.send req uri method data (when headers (clj->js headers)))))

