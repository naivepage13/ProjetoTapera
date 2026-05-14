package br.com.tapera.controller;

import br.com.tapera.model.Denuncia;
import br.com.tapera.repository.JsonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DenunciaController {

    private static final String ARQUIVO = "denuncias.json";
    private static final List<String> STATUS_VALIDOS = List.of("aberta", "em_analise", "resolvida");

    private final JsonRepository repo;

    public DenunciaController(JsonRepository repo) {
        this.repo = repo;
    }

    // ── GET /api/denuncias ─────────────────────────────────────────────────
    @GetMapping("/denuncias")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer limit) {

        List<Denuncia> lista = repo.ler(ARQUIVO, new TypeReference<>() {});

        if (tipo   != null) lista = lista.stream().filter(d -> tipo.equals(d.getTipo())).collect(Collectors.toList());
        if (status != null) lista = lista.stream().filter(d -> status.equals(d.getStatus())).collect(Collectors.toList());

        lista.sort(Comparator.comparing(Denuncia::getCriadoEm).reversed());

        if (limit != null && limit > 0) lista = lista.subList(0, Math.min(limit, lista.size()));

        return ResponseEntity.ok(Map.of("total", lista.size(), "denuncias", lista));
    }

    // ── GET /api/denuncias/{id} ────────────────────────────────────────────
    @GetMapping("/denuncias/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        return repo.ler(ARQUIVO, new TypeReference<List<Denuncia>>() {})
                .stream().filter(d -> id.equals(d.getId())).findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Denúncia não encontrada.")));
    }

    // ── POST /api/denuncias ────────────────────────────────────────────────
    @PostMapping("/denuncias")
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        String titulo      = (String) body.get("titulo");
        String tipo        = (String) body.get("tipo");
        String descricao   = (String) body.get("descricao");
        String localizacao = (String) body.get("localizacao");
        Object latObj      = body.get("lat");
        Object lngObj      = body.get("lng");

        if (titulo == null || tipo == null || descricao == null
                || localizacao == null || latObj == null || lngObj == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: titulo, tipo, descricao, lat, lng, localizacao."));
        }

        double lat = ((Number) latObj).doubleValue();
        double lng = ((Number) lngObj).doubleValue();

        Denuncia nova = new Denuncia();
        nova.setId(UUID.randomUUID().toString());
        nova.setTitulo(titulo);
        nova.setTipo(tipo);
        nova.setDescricao(descricao);
        nova.setLocalizacao(localizacao);
        nova.setReferencia((String) body.getOrDefault("referencia", null));
        nova.setFotoUrl((String) body.getOrDefault("fotoUrl", null));
        nova.setCoordenadas(new Denuncia.Coordenadas(lat, lng));
        nova.setStatus("aberta");
        nova.setCriadoEm(Instant.now());
        nova.setAtualizadoEm(Instant.now());

        List<Denuncia> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        lista.add(nova);
        repo.salvar(ARQUIVO, lista);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Denúncia registrada com sucesso!", "denuncia", nova));
    }

    // ── PATCH /api/denuncias/{id}/status ──────────────────────────────────
    @PatchMapping("/denuncias/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable String id,
                                             @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || !STATUS_VALIDOS.contains(status)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Status inválido. Use: " + String.join(", ", STATUS_VALIDOS)));
        }

        List<Denuncia> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        for (Denuncia d : lista) {
            if (id.equals(d.getId())) {
                d.setStatus(status);
                d.setAtualizadoEm(Instant.now());
                repo.salvar(ARQUIVO, lista);
                return ResponseEntity.ok(Map.of("mensagem", "Status atualizado!", "denuncia", d));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", "Denúncia não encontrada."));
    }

    // ── DELETE /api/denuncias/{id} ─────────────────────────────────────────
    @DeleteMapping("/denuncias/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id) {
        List<Denuncia> lista = repo.ler(ARQUIVO, new TypeReference<>() {});
        boolean removido = lista.removeIf(d -> id.equals(d.getId()));
        if (!removido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Denúncia não encontrada."));
        }
        repo.salvar(ARQUIVO, lista);
        return ResponseEntity.ok(Map.of("mensagem", "Denúncia excluída com sucesso."));
    }

    // ── GET /api/stats ─────────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        List<Denuncia> lista = repo.ler(ARQUIVO, new TypeReference<>() {});

        Map<String, Long> porStatus = lista.stream()
                .collect(Collectors.groupingBy(Denuncia::getStatus, Collectors.counting()));

        Map<String, Long> porTipo = lista.stream()
                .collect(Collectors.groupingBy(Denuncia::getTipo, Collectors.counting()));

        return ResponseEntity.ok(Map.of(
                "total", lista.size(),
                "porStatus", porStatus,
                "porTipo", porTipo
        ));
    }
}
