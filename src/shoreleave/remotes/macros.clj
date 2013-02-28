(ns shoreleave.remotes.macros
  "Macros to smooth over the use of RPC")

;; The macro calls are preferred to the raw calls.
;; Handling the "remote-name" correctly can be troublesome,
;; and the macro ensures uniform handling.

(defmacro rpc
  [[sym & params] & [destruct & body]]
  (let [func (if destruct
               (if (some #{:on-success :on-error} body)
                 (reduce (fn [callback-map [k v-form]] (assoc callback-map k `(fn ~destruct ~v-form)))
                         {} (apply hash-map body))
                 `(fn ~destruct ~@body))
               nil)]
    `(shoreleave.remotes.http-rpc/remote-callback ~(str sym)
                                                  ~(vec params)
                                                  ~func)))

(defmacro letrpc
  [bindings & body]
  (let [bindings (partition 2 bindings)]
    (reduce
      (fn [prev [destruct func]]
        `(rpc ~func [~destruct] ~prev))
      `(do ~@body)
      (reverse bindings))))

