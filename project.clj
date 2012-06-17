(defproject shoreleave-remote "0.2.1"
  :description "A smarter client-side with ClojureScript : Shoreleave's rpc/xhr/jsonp facilities"
  :url "http://github.com/shoreleave"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "See the notice in README.mkd or details in LICENSE_epl.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [shoreleave/shoreleave-core "0.2.1"]
                 [shoreleave/shoreleave-browser "0.2.1"]]
  :dev-dependencies [[vimclojure/server "2.3.3" :exclusions [org.clojure/clojure]] 
                     ;[cdt "1.2.6.2-SNAPSHOT"]
                     ;[lein-cdt "1.0.0"] ; use lein cdt to attach
                     ;[lein-autodoc "0.9.0"]
                     [lein-marginalia "0.7.1"]]
  :plugins  [[lein-cljsbuild "0.2.1"]])

