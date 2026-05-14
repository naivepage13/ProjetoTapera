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

    // ── POST /api/contato ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> enviar(@RequestBody Map<String, String> body) {
        String assunto  = body.get("assunto");
        String mensagem = body.get("mensagem");

        if (assunto == null || mensagem == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: assunto, mensagem."));
        }
        if (!ASSUNTOS_VALIDOS.contains(assunto)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Assunto inválido. Use: " + String.join(", ", ASSUNTOS_VALIDOS)));
        }
        if (mensagem.trim().length() < 10) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A mensagem deve ter pelo menos 10 caracteres."));
        }

        Mensagem nova = new Mensagem();
        nova.setId(UUID.randomUUID().toString());
        nova.setAssunto(assunto);
        nova.setMensagem(mensagem.trim());
        nova.setNome(body.getOrDefault("nome", null));
        nova.setEmail(body.getOrDefault("email", null));
        nova.setLida(false);
        nova.setCriadoEm(Instant.now());

        List<Mensagem> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.add(nova);
        repo.salvar(ARQUIVO, lista);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Mensagem enviada com sucesso!", "id", nova.getId()));
    }

    // ── GET /api/contato ───────────────────────────────────────────────────
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

    // ── PATCH /api/contato/{id}/lida ──────────────────────────────────────
    @PatchMapping("/{id}/lida")
    public ResponseEntity<?> marcarLida(@PathVariable String id) {
        List<Mensagem> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        for (Mensagem m : lista) {
            if (id.equals(m.getId())) {
                m.setLida(true);
                repo.salvar(ARQUIVO, lista);
                return ResponseEntity.ok(Map.of("mensagem", "Mensagem marcada como lida.", "item", m));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", "Mensagem não encontrada."));
    }

    // ── DELETE /api/contato/{id} ───────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id) {
        List<Mensagem> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        boolean removido = lista.removeIf(m -> id.equals(m.getId()));
        if (!removido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Mensagem não encontrada."));
        }
        repo.salvar(ARQUIVO, lista);
        return ResponseEntity.ok(Map.of("mensagem", "Mensagem excluída com sucesso."));
    }
}
