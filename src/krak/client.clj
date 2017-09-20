(ns krak.client
  (:require [krak.intern.client :refer :all]))

(defn new-context [api-key secret-key & {:keys [nonce-offset rate-counter]
                                         :or {nonce-offset 0 rate-counter 0}}]
  (atom {:api-key api-key
         :secret-key secret-key
         :nonce-offset (long nonce-offset)}))

(def time (public-req "/0/public/Time" {}))
(def assets
  (public-req "/0/public/Assets"
              {:info {:optional true}
               :aclass {:optional true}
               :asset {:optional true}}))
(def asset-pairs
  (public-req "/0/public/AssetPairs"
              {:info {:optional true}
               :pair {:optional true :prepare-fn comma-join}}))
(def ticker
  (public-req "/0/public/Ticker"
              {:pair {:optional true :prepare-fn comma-join}}))
(def ohlc
  (public-req "/0/public/OHLC"
              {:pair {} :interval {:optional true} :since {:optional true}}))
(def depth (public-req "/0/public/Depth" {:pair {} :count {:optional true}}))
(def trades (public-req "/0/public/Trades" {:pair {} :since {:optional true}}))
(def spread (public-req "/0/public/Spread" {:pair {} :since {:optional true}}))

;; PRIVATE

(def balance (private-req "/0/private/Balance" {}))
(def trade-balance
  (private-req "/0/private/TradeBalance" {:aclass {:optional true} :asset {}}))
(def open-orders
  (private-req "/0/private/OpenOrders"
               {:trades {:optional true} :userref {:optional true}}))
(def closed-orders
  (private-req "/0/private/ClosedOrders"
               {:trades {:optional true}
                :userref {:optional true}
                :start {:optional true}
                :end {:optional true}
                :ofs {}
                :closetime {:optional true}}))
(def query-orders
  (private-req "/0/private/QueryOrders"
               {:trades {:optional true}
                :userref {:optional true}
                :txid {:prepare-fn comma-join}}))
(def trades-history
  (private-req "/0/private/TradesHistory"
               {:type {:optional true}
                :trades {:optional true}
                :start {:optional true}
                :end {:optional true}
                :ofs {}}))
(def query-trades
  (private-req "/0/private/QueryTrades"
               {:trades {:optional true} :txid {:prepare-fn comma-join}}))
(def open-positions
  (private-req "/0/private/OpenPositions"
               {:txid {:prepare-fn comma-join} :docalcs {:optional true}}))
(def ledgers
  (private-req "/0/private/Ledgers"
               {:aclass {:optional true}
                :asset {:optional true}
                :type {:optional true}
                :start {:optional true}
                :end {:optional true}
                :ofs {}}))
(def query-ledgers
  (private-req "/0/private/QueryLedgers" {:id {:prepare-fn comma-join}}))
(def trade-volume
  (private-req "/0/private/TradeVolume"
               {:pair {:optional true :prepare-fn comma-join}
                :fee-info {:optional true}}))
(def add-order
  (private-req "/0/private/AddOrder"
               {:pair {}
                :type {}
                :ordertype {}
                :price {:optional true}
                :price2 {:optional true}
                :volume {}
                :leverage {:optional true}
                :oflags {:optional true}
                :starttm {:optional true}
                :expiretm {:optional true}
                :userref {:optional true}
                :validate {:optional true}}))
(def cancel-order (private-req "/0/private/CancelOrder" {:txid {}}))

;; FUNDING

(def deposit-methods
  (private-req "/0/private/DepositMethods" {:aclass {:optional true} :asset {}}))
(def deposit-addresses
  (private-req "/0/private/DepositAdresses"
               {:aclass {:optional true}
                :asset {}
                :method {}
                :new {:optional true}}))
(def deposit-status
  (private-req "/0/private/DepositStatus"
               {:aclass {:optional true} :asset {} :method {}}))
(def withdraw-info
  (private-req "/0/private/WithdrawInfo"
               {:aclass {:optional true} :asset {} :key {} :amount {}}))
(def withdraw
  (private-req "/0/private/Withdraw"
               {:aclass {:optional true} :asset {} :key {} :amount {}}))
(def withdraw-status
  (private-req "/0/private/WithdrawStatus"
               {:aclass {:optional true} :asset {} :method {:optional true}}))
(def withdraw-cancel
  (private-req "/0/private/WithdrawCancel"
               {:aclass {:optional true} :asset {} :refid {}}))
