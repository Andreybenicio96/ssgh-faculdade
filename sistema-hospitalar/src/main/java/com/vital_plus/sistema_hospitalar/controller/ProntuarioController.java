package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.ProntuarioDTO;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.model.Prontuario;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProntuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Cadastrar um prontuário para um paciente
     */
    @PostMapping
    public ResponseEntity<Prontuario> salvar(@Valid @RequestBody ProntuarioDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        Prontuario prontuario = new Prontuario();
        prontuario.setPaciente(paciente);
        prontuario.setDescricao(dto.getDescricao());
        prontuario.setObservacoes(dto.getObservacoes());

        Prontuario salvo = prontuarioRepository.save(prontuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * Listar todos os prontuários
     */
    @GetMapping
    public ResponseEntity<List<Prontuario>> listarTodos() {
        return ResponseEntity.ok(prontuarioRepository.findAll());
    }

    /**
     * Listar prontuários por paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Prontuario>> listarPorPaciente(@PathVariable Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prontuarioRepository.findByPacienteId(pacienteId));
    }
}
