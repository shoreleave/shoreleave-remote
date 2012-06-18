(ns shoreleave.remote
  "Shoreleave's remote calling library"
  (:require [shoreleave.remotes.request :as request]
            [shoreleave.remotes.jsonp :as jsonp]
            [shoreleave.remotes.http-rpc :as rpc]
            [shoreleave.remotes.jquery :as jq]))

;; Remotes
;; -------
;;
;; A major part of complex client-side applications is remote calling.
;;
;; Shoreleave provides a consistent set of arguments across different
;; types of calls: XmlHTTPRequests (xhr), pooled xhr (request), JSONP, and RPC.
;; Additionally all calls to your own server are *CSRF-protected* if you're using
;; the anti-forgery ring middleware.
;;
;; Additionally, if you use the jQuery remote calls, you'll want to to set
;; you `cljsbuild` config accordingly for advanced compilation:
;;
;;      {
;;       :optimizations :advanced
;;       :externs ["externs/sljquery.js"]
;;       ...
;;      }

;; ###`request`
;; This is an XHR request that uses a pool of XHR handlers
;; You should always prefer to use this method over others
(def request request/request)

;; ###`jsonp`
;; JSONP is an excellent way to make cross-origin calls without setting up
;; security certificates.  It relies upon you blindly evaluating the results,
;; so you should only use it with sources you trust.
;;
;; One great application is SOLR.  You can setup a SOLR server and pull search
;; results directly into your client with JSONP
(def jsonp jsonp/jsonp)

;; ###`remote-callback`
;; The foundation of the RPC system is a `remote-callback`.  This is a great way
;; to expose server-side functionality to the client.  A server's remote function
;; is called, and the results are sent back over xhr.  All forms of Clojure data
;; are supported
(def remote-callback rpc/remote-callback)

(defn jq-jsonp
  "Use jQuery's ajax calls to perform a JSONP request"
  [uri & opts]
  (let [{:keys [on-success content callback-name]} opts]
    (jq/ajax uri
             :data content
             :success #(on-success (js->clj % :keywordize-keys true))
             :dataType "jsonp"
             :jsonp callback-name)))

