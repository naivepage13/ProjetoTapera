package br.com.tapera.controller;

import br.com.tapera.model.Usuario;
import br.com.tapera.repository.JsonRepository;
import br.com.tapera.security.JwtUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String ARQUIVO = "usuarios.json";

    private final JsonRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(JsonRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repo            = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    // ── POST /api/auth/cadastro ────────────────────────────────────────────
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Map<String, Object> body) {
        String nome            = (String) body.get("nome");
        String email           = (String) body.get("email");
        String senha           = (String) body.get("senha");
        String confirmarSenha  = (String) body.get("confirmarSenha");

        if (nome == null || email == null || senha == null || confirmarSenha == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: nome, email, senha, confirmarSenha."));
        }
        if (!senha.equals(confirmarSenha)) {
            return ResponseEntity.badRequest().body(Map.of("erro", "As senhas não coincidem."));
        }
        if (senha.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A senha deve ter pelo menos 6 caracteres."));
        }

        List<Usuario> usuarios = repo.ler(ARQUIVO, new TypeReference<>() {});
        if (usuarios.stream().anyMatch(u -> email.equals(u.getEmail()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe uma conta com este e-mail."));
        }

        Usuario novo = new Usuario();
        novo.setId(UUID.randomUUID().toString());
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setSenhaHash(passwordEncoder.encode(senha));
        novo.setAceitaTermos(Boolean.TRUE.equals(body.get("aceitaTermos")));
        novo.setCriadoEm(Instant.now());

        usuarios.add(novo);
        repo.salvar(ARQUIVO, usuarios);

        String token = jwtUtil.gerar(novo.getId(), novo.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensagem", "Conta criada com sucesso!",
                "token",    token,
                "usuario",  Map.of("id", novo.getId(), "nome", novo.getNome(), "email", novo.getEmail())
        ));
    }

    // ── POST /api/auth/login ───────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Campos obrigatórios: email, senha."));
        }

        List<Usuario> usuarios = repo.ler(ARQUIVO, new TypeReference<>() {});
        Optional<Usuario> opt = usuarios.stream().filter(u -> email.equals(u.getEmail())).findFirst();

        // Mensagem genérica para não revelar se o e-mail existe
        if (opt.isEmpty() || !passwordEncoder.matches(senha, opt.get().getSenhaHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "E-mail ou senha incorretos."));
        }

        Usuario u = opt.get();
        String token = jwtUtil.gerar(u.getId(), u.getEmail());

        return ResponseEntity.ok(Map.of(
                "mensagem", "Login realizado com sucesso!",
                "token",    token,
                "usuario",  Map.of("id", u.getId(), "nome", u.getNome(), "email", u.getEmail())
        ));
    }

    // ── GET /api/auth/me ───────────────────────────────────────────────────
    // Rota protegida pelo JwtFilter + SecurityConfig (requer Bearer token)
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        // O principal vem no formato "id:email" definido pelo JwtFilter
        String principal = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        String userId = principal.split(":")[0];

        List<Usuario> usuarios = repo.ler(ARQUIVO, new TypeReference<>() {});
        return usuarios.stream().filter(u -> userId.equals(u.getId())).findFirst()
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of(
                        "id",        u.getId(),
                        "nome",      u.getNome(),
                        "email",     u.getEmail(),
                        "criadoEm",  u.getCriadoEm()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Usuário não encontrado.")));
    }
}
