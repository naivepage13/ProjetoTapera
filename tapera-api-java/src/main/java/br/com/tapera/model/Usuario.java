package br.com.tapera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Usuario {

    private String id;
    private String nome;
    private String email;

    @JsonIgnore
    private String senhaHash;

    private boolean aceitaTermos;
    private Instant criadoEm;
}
