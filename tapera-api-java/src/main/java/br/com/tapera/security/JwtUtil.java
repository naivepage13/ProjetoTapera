package br.com.tapera.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(
            @Value("${tapera.jwt.secret}") String secret,
            @Value("${tapera.jwt.expiration-days:7}") int expirationDays
    ) {
        // Garante chave com pelo menos 256 bits para HS256
        String padded = secret.length() >= 32
                ? secret
                : String.format("%-32s", secret).replace(' ', '_');
        this.key = Keys.hmacShaKeyFor(padded.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = (long) expirationDays * 24 * 60 * 60 * 1000;
    }

    /** Gera um token JWT com id e email no payload. */
    public String gerar(String id, String email) {
        return Jwts.builder()
                .subject(id)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Valida o token e retorna os claims. Lança exceção se inválido. */
    public Claims validar(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Extrai o ID do usuário do token (sem lançar exceção — retorna null se inválido). */
    public String extrairId(String token) {
        try {
            return validar(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
