package org.example;

import io.javalin.Javalin;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

        // Armazena os alunos em memória: ID -> (nome, matrícula)
    private static final Map<Integer, Map<String, String>> alunos = new LinkedHashMap<>();
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(7000);

        // Página de cadastro
        app.get("/cad", ctx -> {
            ctx.html(gerarFormulario(null));
        });

        // Criar novo aluno
        app.post("/alunos", ctx -> {
            String nome = ctx.formParam("nome");
            String matricula = ctx.formParam("matricula");

            if (nome == null || nome.trim().isEmpty() ||
                matricula == null || matricula.trim().isEmpty()) {
                ctx.html(gerarFormulario("Nome e matrícula são obrigatórios."));
                return;
            }

            int id = contadorId.getAndIncrement();
            Map<String, String> dados = new HashMap<>();
            dados.put("nome", nome.trim());
            dados.put("matricula", matricula.trim());
            alunos.put(id, dados);

            // Mostrar o mesmo formulário com mensagem de sucesso
            ctx.html(gerarFormulario("Aluno cadastrado com sucesso!"));
        });

        // Listar alunos
        app.get("/alunos", ctx -> {
            StringBuilder html = new StringBuilder("<h1>Lista de Alunos</h1><ul>");
            for (Map.Entry<Integer, Map<String, String>> entry : alunos.entrySet()) {
                int id = entry.getKey();
                Map<String, String> dados = entry.getValue();
                html.append("<li>")
                        .append("ID: ").append(id)
                        .append(" | Nome: ").append(dados.get("nome"))
                        .append(" | Matrícula: ").append(dados.get("matricula"))
                        .append("</li>");
            }
            html.append("</ul><a href='/cad'>Cadastrar Novo</a>");
            ctx.html(html.toString());
        });
    }


    
}
