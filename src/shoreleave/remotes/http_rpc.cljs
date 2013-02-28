(ns shoreleave.remotes.http-rpc
  "Remote procedure calls over HTTP"
  (:require [shoreleave.remotes.xhr :as xhr]
            ;[goog.structs.PriorityPool :as priority]
            [cljs.reader :as reader]))

;; HTTP-RPC
;; ---------
;; Shoreleave's HTTP-RPC is based on Chris Granger's [fetch](https://github.com/ibdknox/fetch)
;;
;; Underneath, CSRF protection is automatically happening.
;;
;; You can also set the resource url to something different (the default is "/_fetch"),
;; but you must use `(binding ...)` forms on the client-side
;;
;; You will most likely use the `remote` macros to make these calls.  Here is an example:
;;
;;      (srm/rpc  (ping)  [pong-response]
;;          (js/alert pong-response]))
;;
;; vs
;;
;;      (srh/remote-callback "ping" [] #(js/alert %))

(def ^:dynamic *remote-uri* "/_shoreleave")

(defn remote-callback
  "Call a remote-callback on the server.
  Arguments:
    remote - a string, the name of the remote on the server (eg. specified with a defremote)
    params - a vector, the parameters to pass to the remote function
    callback - a map or a function.  The map specifies {:on-success some-f, :on-error another-f}
                otherwise, just a single function that will be called with on-complete is triggered
    extra-content - varlist of key-value pairs, extra-content to merge into the payload/content map."
  [remote params callback & extra-content]
  (if (map? callback)
    (let [{:keys [on-success on-error]} callback] ;;TODO make xhr take *ANY* of the event triggers
      (xhr/xhr [:post *remote-uri*]
               :content (merge
                          {:remote remote
                           :params (pr-str params)}
                          (apply hash-map extra-content))
               :on-success (when on-success
                             (fn [data]
                               (let [data (if (= data "") "nil" data)]
                                 (on-success (reader/read-string data)))))
               :on-error (when on-error
                           (fn [data]
                             (let [data (if (= data "") "nil" data)]
                               (on-error (reader/read-string data)))))))
    (xhr/xhr [:post *remote-uri*]
             :content (merge
                        {:remote remote
                         :params (pr-str params)}
                        (apply hash-map extra-content))
             :on-success (when callback
                           (fn [data]
                             (let [data (if (= data "") "nil" data)]
                               (callback (reader/read-string data))))))))

