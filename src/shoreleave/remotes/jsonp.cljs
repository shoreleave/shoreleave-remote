(ns shoreleave.remotes.jsonp
  "Shoreleave's JSONP"
  (:require [goog.net.Jsonp :as jsonp]
            [shoreleave.common :as common]))

;; JSON with padding - JSONP
;; -------------------------
;; JSONP is a conveinent and widely supported way to make
;; cross origin calls.  Shoreleave's support is built Closure's [JSONP Object](http://closure-library.googlecode.com/svn/docs/class_goog_net_Jsonp.html).
;;
;; The `(jsonp ...)` function takes one mandatory `uri` string argument.
;;
;; Additional options are passed in as key'd args:
;;
;;  * :on-success `(fn [result] (js/console.log "This is a callback function for successful requests"))`
;;  * :on-timeout - just like above
;;  * :content `{:one-arg "Sending this to the server" :another 5}`
;;  * :timeout-ms - the number of milliseconds until the call times out
;;  * :callback-value - hand-set the callback param value if your server requires something specific 
;;  * :callback-name - the callback's name (there's usually no reason to set this)
;;
;;  *NOTE:* - SOLR requires a callback-name of "json.wrf"

(defn jsonp [uri & opts]
  (let [{:keys [on-success on-timeout content callback-name callback-value timeout-ms]} opts
        req (goog.net.Jsonp. uri callback-name)
        data (when content (common/clj->js content))
        on-success (when on-success #(on-success (js->clj % :keywordize-keys true)))
        on-timeout (when on-timeout #(on-timeout (js->clj % :keywordize-keys true)))
        ]
    (when timeout-ms (.setRequestTimeout req timeout-ms))
    (.send req data on-success on-timeout callback-value)))

