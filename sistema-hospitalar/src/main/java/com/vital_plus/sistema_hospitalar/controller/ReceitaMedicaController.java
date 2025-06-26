package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.ReceitaMedicaDTO;
import com.vital_plus.sistema_hospitalar.model.ReceitaMedica;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import com.vital_plus.sistema_hospitalar.repository.ReceitaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/receitas")
public class ReceitaMedicaController {

    @Autowired
    private ReceitaMedicaRepository receitaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @PostMapping
    public ResponseEntity<?> emitirReceita(@RequestBody ReceitaMedicaDTO dto) {
        var paciente = pacienteRepository.findById(dto.getPacienteId());
        var profissional = profissionalRepository.findById(dto.getProfissionalId());

        if (paciente.isEmpty() || profissional.isEmpty()) {
            return ResponseEntity.badRequest().body("Paciente ou Profissional não encontrado.");
        }

        ReceitaMedica receita = ReceitaMedica.builder()
                .paciente(paciente.get())
                .profissionalSaude(profissional.get())
                .conteudo(dto.getConteudo())
                .dataEmissao(LocalDate.now())
                .build();

        return ResponseEntity.ok(receitaRepository.save(receita));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ReceitaMedica>> listarPorPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(receitaRepository.findByPacienteId(id));
    }

    @GetMapping("/profissional/{id}")
    public ResponseEntity<List<ReceitaMedica>> listarPorProfissional(@PathVariable Long id) {
        return ResponseEntity.ok(receitaRepository.findByProfissionalSaudeId(id));
    }
}
