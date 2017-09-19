(ns krak.client
  (:require [krak.intern.client :refer :all]))


(defn new-context [api-key secret-key & {:keys [nonce-offset rate-counter]
                                         :or {nonce-offset 0 rate-counter 0}}]
  (atom {:api-key api-key
         :secret-key secret-key
         :rate-counter rate-counter
         :nonce-offset (long nonce-offset)}))

(def endpoints
  [
   ;; PUBLIC
   {:fn-name 'time
    :path "/0/public/Time"
    :inputs {}}
   {:fn-name 'assets
    :path "/0/public/Assets"
    :inputs {:info {:optional true}
             :aclass {:optional true}
             :asset {:optional true}}}
   {:fn-name 'asset-pairs
    :path "/0/public/AssetPairs"
    :inputs {:info {:optional true
                    :vals ["info" "leverage" "fees" "margin"]}
             :pair {:optional true
                    :prepare-fn cj}}}
   {:fn-name 'ticker
    :path "/0/public/Ticker"
    :inputs {:pair {:optional true
                    :prepare-fn cj}}}
   {:fn-name 'ohlc
    :path "/0/public/OHLC"
    :inputs {:pair {}
             :interval {:optional true}
             :since {:optional true}}}
   {:fn-name 'depth
    :path "/0/public/Depth"
    :inputs {:pair {}
             :count {:optional true}}}
   {:fn-name 'trades
    :path "/0/public/Trades"
    :inputs {:pair {}
             :since {:optional true}}}
   {:fn-name 'spread
    :path "/0/public/Spread"
    :inputs {:pair {}
             :since {:optional true}}}

   ;; PRIVATE
   {:fn-name 'balance
    :path "/0/private/Balance"
    :signature true
    :inputs {}}
   {:fn-name 'trade-balance
    :path "/0/private/TradeBalance"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}}}
   {:fn-name 'open-orders
    :path "/0/private/OpenOrders"
    :signature true
    :inputs {:trades {:optional true}
             :userref {:optional true}}}
   {:fn-name 'closed-orders
    :path "/0/private/ClosedOrders"
    :signature true
    :inputs {:trades {:optional true}
             :userref {:optional true}
             :start {:optional true}
             :end {:optional true}
             :ofs {}
             :closetime {:optional true}}}
   {:fn-name 'query-orders
    :path "/0/private/QueryOrders"
    :signature true
    :inputs {:trades {:optional true}
             :userref {:optional true}
             :txid {:prepare-fn cj}}}
   {:fn-name 'trades-history
    :path "/0/private/TradesHistory"
    :signature true
    :inputs {:type {:optional true}
             :trades {:optional true}
             :start {:optional true}
             :end {:optional true}
             :ofs {}}}
   {:fn-name 'query-trades
    :path "/0/private/QueryTrades"
    :signature true
    :inputs {:trades {:optional true}
             :txid {:prepare-fn cj}}}
   {:fn-name 'open-positions
    :path "/0/private/OpenPositions"
    :signature true
    :inputs {:txid {:prepare-fn cj}
             :docalcs {:optional true}}}
   {:fn-name 'ledgers
    :path "/0/private/Ledgers"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {:optional true}
             :type {:optional true}
             :start {:optional true}
             :end {:optional true}
             :ofs {}}}
   {:fn-name 'query-ledgers
    :path "/0/private/QueryLedgers"
    :signature true
    :inputs {:id {:prepare-fn cj}}}
   {:fn-name 'trade-volume
    :path "/0/private/TradeVolume"
    :signature true
    :inputs {:pair {:optional true
                    :prepare-fn cj}
             :fee-info {:optional true}}}
   {:fn-name 'add-order
    :path "/0/private/AddOrder"
    :signature true
    :inputs {:pair {}
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
             :validate {:optional true}}}
   {:fn-name 'cancel-order
    :path "/0/private/CancelOrder"
    :signature true
    :inputs {:txid {}}}

   ;; FUNDING
   {:fn-name 'deposit-methods
    :path "/0/private/DepositMethods"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}}}
   {:fn-name 'deposit-addresses
    :path "/0/private/DepositAdresses"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :method {}
             :new {:optional true}}}
   {:fn-name 'deposit-status
    :path "/0/private/DepositStatus"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :method {}}}
   {:fn-name 'withdraw-info
    :path "/0/private/WithdrawInfo"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :key {}
             :amount {}}}
   {:fn-name 'withdraw
    :path "/0/private/Withdraw"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :key {}
             :amount {}}}
   {:fn-name 'withdraw-status
    :path "/0/private/WithdrawStatus"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :method {:optional true}}}
   {:fn-name 'withdraw-cancel
    :path "/0/private/WithdrawCancel"
    :signature true
    :inputs {:aclass {:optional true}
             :asset {}
             :refid {}}}
   ])

(dorun (map register-endpoint endpoints))
