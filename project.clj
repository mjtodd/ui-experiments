(defproject atest "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
  	[org.clojure/clojure "1.5.1"]
  	[org.clojure/clojurescript "0.0-1896"]
  	[jayq "2.4.0"]
  	[crate "0.2.4"]
  	[org.clojure/core.async "0.1.242.0-44b1e3-alpha"]
  	]
  :plugins [[lein-cljsbuild "0.3.3"]]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["src-cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "target/main.js"  ; default: target/cljsbuild-main.js
          :optimizations :whitespace
          ;:source-map "main.js.map"
          :pretty-print true}}]}
  :main atest.core)
