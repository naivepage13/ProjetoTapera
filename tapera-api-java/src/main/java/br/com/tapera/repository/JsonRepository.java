package br.com.tapera.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lê e salva listas de objetos em arquivos JSON — equivalente às funções
 * lerJSON() e salvarJSON() do server.js original.
 */
@Component
public class JsonRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonRepository.class);

    private final ObjectMapper mapper;
    private final File dbDir;

    public JsonRepository(@Value("${tapera.db.dir:./dados}") String dbDir) {
        this.dbDir = new File(dbDir);
        if (!this.dbDir.exists()) {
            this.dbDir.mkdirs();
        }
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    /** Lê um arquivo JSON como lista. Retorna lista vazia se não existir ou em caso de erro. */
    public <T> List<T> ler(String nomeArquivo, TypeReference<List<T>> tipo) {
        File arquivo = new File(dbDir, nomeArquivo);
        if (!arquivo.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(arquivo, tipo);
        } catch (IOException e) {
            log.error("Erro ao ler {}: {}", nomeArquivo, e.getMessage());
            return new ArrayList<>();
        }
    }

    /** Salva uma lista em arquivo JSON. */
    public <T> void salvar(String nomeArquivo, List<T> dados) {
        File arquivo = new File(dbDir, nomeArquivo);
        try {
            mapper.writeValue(arquivo, dados);
        } catch (IOException e) {
            log.error("Erro ao salvar {}: {}", nomeArquivo, e.getMessage());
            throw new RuntimeException("Erro ao persistir dados em " + nomeArquivo, e);
        }
    }
}
