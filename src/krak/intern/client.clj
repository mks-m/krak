(ns krak.intern.client
  (:require [org.httpkit.client :as http]
            [pandect.algo.sha512 :refer [sha512-hmac-bytes]]
            [pandect.algo.sha256 :refer [sha256-bytes]]
            [clojure.data.codec.base64 :as b64]
            [clojure.data.json :as json]
            [clojure.set :as set]))

(def KRAKEN-API "https://api.kraken.com")

(defn comma-join [coll] (if (empty? coll) nil (clojure.string/join "," coll)))

;; API-Key = API key
;; API-Sign = Message signature using HMAC-SHA512 of
;;   (URI path + SHA256(nonce + POST data)) and base64 decoded secret API key
(defn- sign-request [api-key secret-key path data nonce]
  (let [secret-bytes (b64/decode (.getBytes secret-key))
        nd-digest (sha256-bytes (str nonce data))
        path-digest (byte-array (concat (.getBytes path) nd-digest))
        hmac-bytes (sha512-hmac-bytes path-digest secret-bytes)
        signature (String. (b64/encode hmac-bytes))]
    {"API-KEY" api-key "API-Sign" signature}))

(defn- prepare-opt [[key {:keys [optional prepare-fn]}] data]
  (if (and (not optional) (nil? data))
    (throw (Exception. (str "must provide " key)))
    [key
     (if-not prepare-fn data
       (try (prepare-fn data)
            (catch Exception e
              (throw (Exception.
                      (str "failed prepare-fn " key " " (ex-data e)))))))]))

(defn- prepare-opts [inputs opts]
  (let [ok (set (keys opts))
        ik (set (keys inputs))]
    (if-not (set/subset? ok ik)
     (throw (Exception. (str "unknown arguments: " (set/difference ok ik))))))
  (into {} (remove (comp nil? second)
                   (map #(prepare-opt % ((first %) opts)) inputs))))

(defn- send-req [path data headers]
  (let [response @(http/post (str KRAKEN-API path)
                             {:body data :headers headers})
        resp-data (json/read-str (:body response))]
    (merge response {:data resp-data
                     :krak/result (resp-data "result")
                     :krak/error (resp-data "error")})))

(defn private-req [path inputs]
  (fn [ctx & {:as opts}]
    (let [{:keys [api-key secret-key nonce-offset]} @ctx
          nonce (+ nonce-offset (System/currentTimeMillis))
          data (#'http/query-string
                (assoc (prepare-opts inputs opts) :nonce nonce))
          headers (sign-request api-key secret-key path data nonce)]
      (send-req path data headers))))

(defn public-req [path inputs]
  (fn [& {:as opts}]
    (send-req path (#'http/query-string (prepare-opts inputs opts)) {})))
