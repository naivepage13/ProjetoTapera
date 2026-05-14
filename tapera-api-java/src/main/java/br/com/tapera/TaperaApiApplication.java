package br.com.tapera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaperaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaperaApiApplication.class, args);
        System.out.println("""

                🌊 Tapera API (Java/Spring Boot) rodando em http://localhost:3000

                ── Denúncias ──────────────────────────────
                  GET    /api/denuncias
                  GET    /api/denuncias/{id}
                  POST   /api/denuncias
                  PATCH  /api/denuncias/{id}/status
                  DELETE /api/denuncias/{id}
                  GET    /api/stats

                ── Fórum ──────────────────────────────────
                  GET    /api/topicos
                  GET    /api/topicos/{id}
                  POST   /api/topicos
                  POST   /api/topicos/{id}/like
                  POST   /api/topicos/{id}/deslike
                  DELETE /api/topicos/{id}

                ── Perfis ─────────────────────────────────
                  GET    /api/perfis
                  GET    /api/perfis/{id}
                  POST   /api/perfis
                  PUT    /api/perfis/{id}
                  DELETE /api/perfis/{id}

                ── Autenticação ───────────────────────────
                  POST   /api/auth/cadastro
                  POST   /api/auth/login
                  GET    /api/auth/me

                ── Contato ────────────────────────────────
                  POST   /api/contato
                  GET    /api/contato
                  PATCH  /api/contato/{id}/lida
                  DELETE /api/contato/{id}
                """);
    }
}
