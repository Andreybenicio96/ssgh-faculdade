package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.ProntuarioDTO;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.model.Prontuario;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProntuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
@Tag(name = "Prontuarios", description = "Gerenciamento de Prontuarios Médicos")
@Slf4j
public class ProntuarioController {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Cadastrar um prontuário para um paciente
     */
    @Operation(summary = "Cadastrar Prontuario", description = "Cadastra um novo prontuario médico para um paciente.")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @PostMapping("/cadastrar/{pacienteId}")
    public ResponseEntity<Prontuario> salvar(@Valid @RequestBody ProntuarioDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        Prontuario prontuario = new Prontuario();
        prontuario.setPaciente(paciente);
        prontuario.setDescricao(dto.getDescricao());
        prontuario.setObservacoes(dto.getObservacoes());

        Prontuario salvo = prontuarioRepository.save(prontuario);
        log.info("Prontuário cadastrado com sucesso: {}", salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * Listar todos os prontuários
     */
    @Operation(summary = "Listar Prontuarios", description = "Lista todos os prontuarios Médicos cadastrados.")
    @GetMapping
    public ResponseEntity<List<Prontuario>> listarTodos() {
        log.info("Listando todos os prontuários");
        return ResponseEntity.ok(prontuarioRepository.findAll());
    }

    /**
     * Listar prontuários por paciente
     */
    @Operation(summary = "Listar Prontuarios por Paciente", description = "Lista todos os prontuarios médicos associados a um paciente específico.")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Prontuario>> listarPorPaciente(@PathVariable Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            log.warn("Paciente não encontrado: {}", pacienteId);
            return ResponseEntity.notFound().build();
        }
        log.info("Listando prontuários para o paciente ID: {}", pacienteId);
        return ResponseEntity.ok(prontuarioRepository.findByPacienteId(pacienteId));
    }
}
