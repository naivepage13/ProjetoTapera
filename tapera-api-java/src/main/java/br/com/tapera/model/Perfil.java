package br.com.tapera.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Perfil {

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String bairro;
    private Instant criadoEm;
    private Instant atualizadoEm;
}
