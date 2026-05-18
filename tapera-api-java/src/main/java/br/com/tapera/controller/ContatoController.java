package br.com.tapera.controller;

import br.com.tapera.model.Mensagem;
import br.com.tapera.repository.JsonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin(origins = "*") // Permite que seu HTML acesse a API sem erro de CORS
public class ContatoController {

    private static final String ARQUIVO = "mensagens.json";
    private static final List<String> ASSUNTOS_VALIDOS = List.of(
            "Dúvida sobre denúncia",
            "Problema técnico no site",
            "Parceria/Projeto",
            "Outro"
    );

    private final JsonRepository repo;

    public ContatoController(JsonRepository repo) {
        this.repo = repo;
    }

    // --- ROTA ANTIGA (Unificada aqui) ---
    @GetMapping("/simples")
    public List<Map<String, Object>> listarSimples() {
        return List.of(
            Map.of("nome", "Thiago", "numero", "123456"),
            Map.of("nome", "Tapera", "numero", "987654")
        );
    }

    // ── POST /api/contato (Enviar mensagem do Formulário HTML) ──
    @PostMapping
    public ResponseEntity<?> enviar(@RequestBody Map<String, String> body) {
        String assunto  = body.get("assunto");
        String mensagem = body.get("mensagem");

        if (assunto == null || mensagem == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Campos obrigatórios: assunto, mensagem."));
        }
        
        // ... (resto das validações que você já tem)

        Mensagem nova = new Mensagem();
        nova.setId(UUID.randomUUID().toString());
        nova.setAssunto(assunto);
        nova.setMensagem(mensagem.trim());
        nova.setNome(body.getOrDefault("nome", "Anônimo"));
        nova.setEmail(body.getOrDefault("email", "N/A"));
        nova.setLida(false);
        nova.setCriadoEm(Instant.now());

        List<Mensagem> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.add(nova);
        repo.salvar(ARQUIVO, lista);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensagem", "Enviada com sucesso!", "id", nova.getId()));
    }

    // ── GET /api/contato (Listar mensagens do JSON) ──
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String lida) {
        List<Mensagem> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.sort(Comparator.comparing(Mensagem::getCriadoEm).reversed());

        if (lida != null) {
            boolean filtro = Boolean.parseBoolean(lida);
            lista = lista.stream().filter(m -> m.isLida() == filtro).collect(Collectors.toList());
        }

        return ResponseEntity.ok(Map.of("total", lista.size(), "mensagens", lista));
    }
    
    // ... Mantenha seus métodos @PatchMapping e @DeleteMapping abaixo ...
}