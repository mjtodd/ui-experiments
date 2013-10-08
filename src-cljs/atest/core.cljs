(ns atest.core
  (:require
   [cljs.core.async :as async
             :refer [<! >! chan close! sliding-buffer put! alts!]]
   [jayq.core :refer [$ append ajax inner $deferred when done resolve pipe on] :as jq]
   [crate.core :as crate]
   [clojure.string :refer [join blank?]]
   [clojure.browser.repl :as repl]
   )
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

($ #(repl/connect "http://localhost:9000/repl"))

(defn data-from-event [event]
  (-> event .-currentTarget $ .data (js->clj :keywordize-keys true)))

(defn event-chan [event selector msg-name]
  (let [rc (chan)]
    (on ($ "body") event selector {}
        (fn [e]
          (jq/prevent e)
          (put! rc [msg-name (data-from-event e)])))
    rc))

(defn merge-chans [& chans]
  (let [rc (chan)]
    (go
     (loop []
       (put! rc (first (alts! chans)))
       (recur)))
    rc))

(defn filter-chan [pred channel]
  (let [rc (chan)]
    (go (loop []
          (let [val (<! channel)]
            (if (pred val) (put! rc val))
            (recur))
          ))
    rc))

(defn render-data [state]
	(.html ($ "#result") 
    (crate/html [:div [:p "result" state]])
    )
	)

(defn ask []
  (let [ok-chan (event-chan :click "#btn2" :ok)
        out-chan (chan)
        form-el ($ "form")]
    (go
      (.show form-el)
      (<! ok-chan)
      (>! out-chan (.val ($ "#in1")))
      (.hide form-el)
      )
    out-chan
    ))

(defn begin []
	(.log js/console "begin")
	(let [btn-click (event-chan :click "#btn" :testmsg)]
		(go
			(loop [state 0]
				(render-data state)
				(let [[msg-type msg-data] (<! btn-click)]
					(.log js/console (str msg-type))
					)
        (.log js/console "you said", (<! (ask)))
				(recur (inc state))
				)
			)
		))

($ begin)