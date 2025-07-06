package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.Exame;
import com.vital_plus.sistema_hospitalar.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.http.*;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exames")
@Tag(name = "Exames", description = "Gerenciamento de Exames Médicos")
@Slf4j
public class ExameController {
    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    /**
     * Endpoint para cadastrar um exame.
     * @param pacienteId ID do paciente associado ao exame
     * @param profissionalId ID do profissional de saúde associado ao exame
     * @param exame Dados do exame a ser cadastrado
     * @return Exame salvo
     */

    @Operation(summary = "Cadastrar Exame", description = "Cadastra um novo exame médico associado a um paciente e profissional de saúde.") 
    @PostMapping("/{pacienteId}/{profissionalId}")
    public ResponseEntity<Exame> salvar(@PathVariable Long pacienteId, @PathVariable Long profissionalId, @Valid @RequestBody Exame exame) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
        if (paciente.isPresent() && profissional.isPresent()) {
            exame.setPaciente(paciente.get());
            exame.setProfissionalSaude(profissional.get());
            Exame salvo = exameRepository.save(exame);
            log.info("Exame cadastrado com sucesso: {}", salvo);
            return ResponseEntity.ok(salvo);
        } else {
            log.warn("Paciente ou profissional de saúde não encontrado: pacienteId={}, profissionalId={}", pacienteId, profissionalId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para listar todos os exames.
     * @return Lista de exames
     */

    @Operation(summary = "Listar Exames", description = "Lista todos os exames médicos cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<List<Exame>> listar() {
        List<Exame> exames = exameRepository.findAll();
        log.info("Listando {} exames", exames.size());
        return ResponseEntity.ok(exames);
    }
}
