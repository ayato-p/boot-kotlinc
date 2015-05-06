(ns ayato_p/boot-kotlinc
  {:boot/export-tasks true}
  (:require [boot.core :as core]
            [boot.pod :as pod])
  (:import [java.io File]
           [java.util Arrays]))

;; [org.jetbrains.kotlin/kotlin-compiler "0.11.91.4"]

(def kotlinc-version "0.11.91.4")

(def ^:private deps
  (delay (remove pod/dependency-loaded? `[[org.jetbrains.kotlin/kotlin-compiler ~kotlinc-version]])))

;; (org.jetbrains.kotlin.cli.jvm.K2JVMCompiler/main (into-array ["-d" "target" "-kotlin-home" "/home/ayato/Downloads/kotlinc/" "src/kotlin/hello.kt" "src/kotlin/hello2.kt" "src/kotlin/hello3.kt"]))

(core/deftask kotlinc
  "Compile kotlin sources."
  []
  (let [pod-env (-> (core/get-env) (update-in [:dependencies] into (vec (seq @deps))))
        pod (future (pod/make-pod pod-env))
        tgt (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (pod/with-call-in pod
       (let [throw?   (atom nil)
             compiler (or org.jetbrains.kotlin.cli.jvm.K2JVMCompiler/main
                          (throw (Exception. "The kotlin compiler is not working. Please make sure you set a kotlin-home!")))
             opts ["-d" (.getPath tgt)]
             srcs (some->> (core/input-files fileset)
                           (core/by-ext [".kt"])
                           (map #(.getPath (core/tmp-file %))))]

         (compiler (into-array String (concat opts srcs)))))
      (-> fileset (core/add-resource tgt) core/commit!))))


