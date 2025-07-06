package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import com.vital_plus.sistema_hospitalar.model.Leito;
import com.vital_plus.sistema_hospitalar.repository.LeitoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/leitos")
@Tag(name = "Leitos", description = "Gerenciamento de Leitos Hospitalares")
@Slf4j
public class LeitoController {
    @Autowired
    private LeitoRepository leitoRepository;

    /**
     * Endpoint para cadastrar um leito.
     * @param leito Dados do leito a ser cadastrado
     * @return Leito salvo
     */
    @Operation(summary = "Cadastar Leito", description = "Cadastra um novo leito hospitalar.")
    @PostMapping("/cadastrar-leito")
    public ResponseEntity<Leito> salvar(@Valid @RequestBody Leito leito) {
        Leito salvo = leitoRepository.save(leito);
        log.info("Leito cadastrado com sucesso: {}", salvo);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Endpoint para listar todos os leitos.
     * @return Lista de leitos
     */
    @Operation(summary = "Listar Leito", description = "Lista todos os leitos hospitalares cadastrados.")
    @GetMapping
    public ResponseEntity<List<Leito>> listar() {
        List<Leito> leitos = leitoRepository.findAll();
        if (leitos.isEmpty()){
            log.warn("Nenhum leito encontrado.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        log.info("Listando todos os leitos. Total: {}", leitos.size());
        return ResponseEntity.ok(leitos);
    }
}
