package br.com.tapera.controller;

import br.com.tapera.model.Perfil;
import br.com.tapera.repository.JsonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private static final String ARQUIVO = "perfis.json";

    private final JsonRepository repo;

    public PerfilController(JsonRepository repo) {
        this.repo = repo;
    }

    // ── GET /api/perfis ────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Perfil> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.sort(Comparator.comparing(Perfil::getCriadoEm).reversed());
        return ResponseEntity.ok(Map.of("total", lista.size(), "perfis", lista));
    }

    // ── GET /api/perfis/{id} ───────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return repo.ler(ARQUIVO, new TypeReference<List<Perfil>>() {})
                .stream().filter(p -> id.equals(p.getId())).findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Perfil não encontrado.")));
    }

    // ── POST /api/perfis ───────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, String> body) {
        String nome  = body.get("nome");
        String email = body.get("email");
        if (nome == null || nome.isBlank() || email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: nome, email."));
        }

        List<Perfil> lista = repo.ler(ARQUIVO, new TypeReference<>() {});

        if (lista.stream().anyMatch(p -> email.equals(p.getEmail()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe um perfil com este e-mail."));
        }

        Perfil novo = new Perfil();
        novo.setId(UUID.randomUUID().toString());
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setTelefone(body.getOrDefault("telefone", null));
        novo.setBairro(body.getOrDefault("bairro", null));
        novo.setCriadoEm(Instant.now());
        novo.setAtualizadoEm(Instant.now());

        lista.add(novo);
        repo.salvar(ARQUIVO, lista);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Perfil cadastrado com sucesso!", "perfil", novo));
    }

    // ── PUT /api/perfis/{id} ───────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable String id,
                                       @RequestBody Map<String, String> body) {
        String nome  = body.get("nome");
        String email = body.get("email");
        if (nome == null || nome.isBlank() || email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: nome, email."));
        }

        List<Perfil> lista = repo.ler(ARQUIVO, new TypeReference<>() {});

        // Verifica duplicidade de e-mail (ignora o próprio perfil)
        boolean duplicado = lista.stream()
                .anyMatch(p -> email.equals(p.getEmail()) && !id.equals(p.getId()));
        if (duplicado) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe outro perfil com este e-mail."));
        }

        for (Perfil p : lista) {
            if (id.equals(p.getId())) {
                p.setNome(nome);
                p.setEmail(email);
                p.setTelefone(body.getOrDefault("telefone", null));
                p.setBairro(body.getOrDefault("bairro", null));
                p.setAtualizadoEm(Instant.now());
                repo.salvar(ARQUIVO, lista);
                return ResponseEntity.ok(Map.of("mensagem", "Perfil atualizado com sucesso!", "perfil", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", "Perfil não encontrado."));
    }

    // ── DELETE /api/perfis/{id} ────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id) {
        List<Perfil> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        boolean removido = lista.removeIf(p -> id.equals(p.getId()));
        if (!removido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Perfil não encontrado."));
        }
        repo.salvar(ARQUIVO, lista);
        return ResponseEntity.ok(Map.of("mensagem", "Perfil excluído com sucesso."));
    }
}
