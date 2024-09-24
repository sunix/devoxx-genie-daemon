package com.devoxx.genie.daemon;

import io.quarkiverse.retrofit.easy.runtime.EnableRetrofit;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@EnableRetrofit(basePackages = "dev.langchain4j.model.ollama")
@QuarkusMain
public class QuarkusApplicationMain {
    public static void main(String[] args) {
    Quarkus.run(DevoxxGenieDaemonApplication.class, args);
    }

    public static class DevoxxGenieDaemonApplication implements QuarkusApplication {
        @Override
        public int run(String... args) throws Exception {
            System.out.println("Do startup logic here");
            Quarkus.waitForExit();
            return 0;
        }
    }
}