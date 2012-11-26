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
;; You will most likely use the macros to make these calls.  Here is an example:
;;
;;      (srm/rpc  (ping)  [pong-response]
;;          (js/alert pong-response]))
;;
;; vs
;;
;;      (srh/remote-callback "ping" [] #(js/alert %))

(def ^:dynamic *remote-uri* "/_fetch")

(defn remote-callback [remote params callback & extra-content]
  (if (map? callback)
    (let [{:keys [on-success on-error]} callback]
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

; TODO I believe there is an error with the xhrManager getting back Clojure data, but I haven't confirmed it
#_(defn remote-callback [remote params callback & extra-content]
  (xhr/request [:post *remote-uri*]
               :content (merge
                          {:remote remote
                           :params (pr-str params)}
                          (apply hash-map extra-content))
               :priority (dec priority/DEFAULT_PRIORITY_)
               :on-success (when callback
                             #(->> % :body
                                (fn [data]
                                  (let [data (if (= data "") "nil" data)]
                                    (callback (reader/read-string data))))))
               :on-error #(->> % :event js/console.log)))
 
