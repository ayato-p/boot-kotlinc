(ns ayato_p.boot-kotlinc
  {:boot/export-tasks true}
  (:require [boot.core :as core]
            [boot.pod :as pod]
            [boot.util :as util])
  (:import [java.io File]
           [java.util Arrays]
           [org.jetbrains.kotlin.cli.jvm K2JVMCompiler]))

;; (def kotlinc-version "0.11.91.4")

;; (def ^:private deps
;;   (delay (remove pod/dependency-loaded? `[[org.jetbrains.kotlin/kotlin-compiler ~kotlinc-version]])))

(core/deftask kotlinc
  "Compile kotlin sources."
  []
  (let [;; pod-env (-> (core/get-env) (update-in [:dependencies] into (vec (seq @deps))))
        ;; pod (future (pod/make-pod pod-env))
        tgt (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (let [opts ["-d" (.getPath tgt)
                  "-kotlin-home" (System/getenv "KOTLIN_HOME")]
            srcs (some->> (core/input-files fileset)
                          (core/by-ext [".kt"])
                          (map #(.getPath (core/tmp-file %))))]
        (when srcs
          (util/info "Compiling %d Kotlin source files...\n" (count srcs))
          (K2JVMCompiler/main (into-array String (concat opts srcs)))))
      (-> fileset (core/add-resource tgt) core/commit!))))
