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
                  POST   /api/denuncias
                
                ── Contato ────────────────────────────────
                  POST   /api/contato
                  GET    /api/contato
                
                🚀 Servidor pronto para receber requisições do seu HTML!
                """);
    }
}