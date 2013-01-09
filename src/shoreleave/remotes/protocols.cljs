(ns shoreleave.remotes.protocols)


;; TransportData
;; --------------
;;
;; To allow full and open interop with Google Closure's lower remote calls,
;; (like XhrIo), the Shoreleave function to package up payloads/contents
;; is a protocol
;;
;; This can be extended or shaped for you application's needs.
;; Out of the box, there is handling for hashmaps and strings.
;;
;; That support/implementation can be found in `remotes/common.cljs`
(defprotocol ITransportData
  (-data-str [t]))

