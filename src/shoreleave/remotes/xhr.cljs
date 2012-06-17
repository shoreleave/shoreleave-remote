(ns shoreleave.remotes.xhr
  (:require [goog.net.XhrIo :as xhr]
            [goog.events :as events]
            [shoreleave.remotes.common :as common])
  (:use [shoreleave.common :only [clj->js]]))

(defn xhr [route & opts]
  (let [req (goog.net.XhrIo.)
        [method uri] (common/parse-route route)
        {:keys [on-success content headers]} (apply hash-map opts)
        content (common/csrf-protected content method)
        data (common/->data-str content)
        callback (common/->simple-callback on-success)]
    (when callback
      (events/listen req goog.net.EventType/COMPLETE #(callback req)))
    (.send req uri method data (when headers (clj->js headers)))))

