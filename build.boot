(set-env! :dependencies '[[org.clojure/clojure "1.6.0" :scope "provided"]
                          [boot/core "2.0.0-rc14" :scope "provided"]
                          [adzerk/bootlaces "0.1.11" :scope "test"]
                          [org.jetbrains.kotlin/kotlin-compiler "0.11.91.4" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.0.1-SNAPSHOT")

(bootlaces! +version+)

(task-options!
 pom  {:project     'ayato_p/boot-kotlinc
       :version     +version+
       :description ""
       :url         "https://github.com/ayato0211/boot-kotlinc"
       :scm         {:url "https://github.com/ayato0211/boot-kotlinc"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})
