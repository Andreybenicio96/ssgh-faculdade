package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.TipoExame;
import com.vital_plus.sistema_hospitalar.repository.TipoExameRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/tipos-exame")
@Tag(name = "Tipos de Exame", description = "Gerenciamento de Tipos de Exames")
@Slf4j
public class TipoExameController {
    @Autowired
    private TipoExameRepository tipoExameRepository;

    /**
     * Endpoint para cadastrar um tipo de exame.
     * @param tipoExame Dados do tipo de exame a ser cadastrado
     * @return Tipo de exame salvo
     */
    @Operation(summary = "Cadastrar Tipo de Exame", description = "Cadastra um novo tipo de exame.")
    @PostMapping
    public ResponseEntity<TipoExame> salvar(@Valid @RequestBody TipoExame tipoExame) {
        TipoExame salvo = tipoExameRepository.save(tipoExame);
        log.info("Tipo de exame cadastrado com sucesso: {}", salvo);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Endpoint para listar todos os tipos de exame.
     * @return Lista de tipos de exame
     */
    @Operation(summary = "Listar Tipos de Exame", description = "Lista todos os tipos de exame cadastrados.")
    @GetMapping
    public ResponseEntity<List<TipoExame>> listar() {
        List<TipoExame> tipos = tipoExameRepository.findAll();
        if (tipos.isEmpty()) {
            log.warn("Nenhum tipo de exame encontrado.");
            return ResponseEntity.noContent().build();
        }
        log.info("Listando todos os tipos de exame. Total: {}", tipos.size());
        return ResponseEntity.ok(tipos);
    }
}
