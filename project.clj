(defproject nomenclature "0.1.0-SNAPSHOT"
  :description "ClojureScript game"
  :main ^:skip-aot app.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [thheller/shadow-cljs "2.11.25"]
                 [reagent "1.0.0"]
                 [cider/cider-nrepl "0.24.0"]
                 [thedavidmeister/xoroshiro128 "1.1.3"]]
  :plugins [[lein-shadow "0.3.1"]
            [lein-cloverage "1.2.2"]]
  :cloverage {:ns-exclude-regex [#"app(\.stats)"]} ; Don't need to test stats file since it won't be used functionally
  :shadow-cljs {:source-paths ["src"]
                :dev-http {8080 "target/"}
                :builds {:app {:output-dir "target/"
                               :asset-path "."
                               :target :browser
                               :modules {:main {:init-fn app.main/main!}}
                               :devtools {:after-load app.main/reload!}}}})
