package br.com.tapera.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Denuncia {

    private String id;
    private String titulo;
    private String tipo;
    private String descricao;
    private String localizacao;
    private String referencia;
    private String fotoUrl;
    private Coordenadas coordenadas;
    private String status = "aberta";
    private Instant criadoEm;
    private Instant atualizadoEm;

    @Data
    @NoArgsConstructor
    public static class Coordenadas {
        private double lat;
        private double lng;

        public Coordenadas(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}
