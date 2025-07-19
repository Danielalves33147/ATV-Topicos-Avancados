package org.example;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
            });
        }).start(7000);

        app.get("/api/status", ctx -> ctx.result("Servidor Javalin rodando com o quiz!"));
    }
}
