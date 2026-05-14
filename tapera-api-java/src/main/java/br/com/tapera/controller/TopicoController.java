package br.com.tapera.controller;

import br.com.tapera.model.Topico;
import br.com.tapera.repository.JsonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {

    private static final String ARQUIVO = "topicos.json";

    private final JsonRepository repo;

    public TopicoController(JsonRepository repo) {
        this.repo = repo;
    }

    // ── GET /api/topicos ───────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer limit) {
        List<Topico> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.sort(Comparator.comparing(Topico::getCriadoEm).reversed());
        if (limit != null && limit > 0) lista = lista.subList(0, Math.min(limit, lista.size()));
        return ResponseEntity.ok(Map.of("total", lista.size(), "topicos", lista));
    }

    // ── GET /api/topicos/{id} ──────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return repo.ler(ARQUIVO, new TypeReference<List<Topico>>() {})
                .stream().filter(t -> id.equals(t.getId())).findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Tópico não encontrado.")));
    }

    // ── POST /api/topicos ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, String> body) {
        String titulo   = body.get("titulo");
        String conteudo = body.get("conteudo");
        String autorId  = body.get("autorId");

        if (titulo == null || conteudo == null || autorId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: titulo, conteudo, autorId."));
        }

        Topico novo = new Topico();
        novo.setId(UUID.randomUUID().toString());
        novo.setTitulo(titulo);
        novo.setConteudo(conteudo);
        novo.setAutorId(autorId);
        novo.setCriadoEm(Instant.now());

        List<Topico> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.add(novo);
        repo.salvar(ARQUIVO, lista);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Tópico publicado com sucesso!", "topico", novo));
    }

    // ── POST /api/topicos/{id}/like ────────────────────────────────────────
    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(@PathVariable String id,
                                  @RequestBody Map<String, String> body) {
        String usuarioId = body.get("usuarioId");
        if (usuarioId == null || usuarioId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Campo obrigatório: usuarioId."));
        }

        List<Topico> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        for (Topico t : lista) {
            if (id.equals(t.getId())) {
                // Toggle: se já curtiu, remove; senão, adiciona e remove do deslike
                if (t.getUsuariosLike().contains(usuarioId)) {
                    t.getUsuariosLike().remove(usuarioId);
                } else {
                    t.getUsuariosLike().add(usuarioId);
                    t.getUsuariosDeslike().remove(usuarioId);
                }
                repo.salvar(ARQUIVO, lista);
                boolean euCurti = t.getUsuariosLike().contains(usuarioId);
                return ResponseEntity.ok(Map.of(
                        "likes",    t.getUsuariosLike().size(),
                        "deslikes", t.getUsuariosDeslike().size(),
                        "euCurti",  euCurti
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Tópico não encontrado."));
    }

    // ── POST /api/topicos/{id}/deslike ─────────────────────────────────────
    @PostMapping("/{id}/deslike")
    public ResponseEntity<?> deslike(@PathVariable String id,
                                     @RequestBody Map<String, String> body) {
        String usuarioId = body.get("usuarioId");
        if (usuarioId == null || usuarioId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Campo obrigatório: usuarioId."));
        }

        List<Topico> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        for (Topico t : lista) {
            if (id.equals(t.getId())) {
                if (t.getUsuariosDeslike().contains(usuarioId)) {
                    t.getUsuariosDeslike().remove(usuarioId);
                } else {
                    t.getUsuariosDeslike().add(usuarioId);
                    t.getUsuariosLike().remove(usuarioId);
                }
                repo.salvar(ARQUIVO, lista);
                boolean euDescurti = t.getUsuariosDeslike().contains(usuarioId);
                return ResponseEntity.ok(Map.of(
                        "likes",      t.getUsuariosLike().size(),
                        "deslikes",   t.getUsuariosDeslike().size(),
                        "euDescurti", euDescurti
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Tópico não encontrado."));
    }

    // ── DELETE /api/topicos/{id} ───────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id) {
        List<Topico> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        boolean removido = lista.removeIf(t -> id.equals(t.getId()));
        if (!removido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Tópico não encontrado."));
        }
        repo.salvar(ARQUIVO, lista);
        return ResponseEntity.ok(Map.of("mensagem", "Tópico excluído com sucesso."));
    }
}
