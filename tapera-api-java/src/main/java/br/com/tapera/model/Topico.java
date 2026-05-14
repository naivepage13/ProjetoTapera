package br.com.tapera.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Topico {

    private String id;
    private String titulo;
    private String conteudo;
    private String autorId;
    private List<String> usuariosLike    = new ArrayList<>();
    private List<String> usuariosDeslike = new ArrayList<>();
    private Instant criadoEm;
}
