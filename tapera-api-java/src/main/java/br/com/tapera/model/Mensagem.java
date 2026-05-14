package br.com.tapera.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Mensagem {

    private String id;
    private String assunto;
    private String mensagem;
    private String nome;
    private String email;
    private boolean lida = false;
    private Instant criadoEm;
}
