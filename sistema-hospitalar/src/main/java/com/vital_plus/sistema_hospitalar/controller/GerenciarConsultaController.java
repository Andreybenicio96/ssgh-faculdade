package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.GerenciarConsultaDTO;
import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;
import com.vital_plus.sistema_hospitalar.model.Consulta;
import com.vital_plus.sistema_hospitalar.repository.ConsultaRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gerenciar-consultas")
public class GerenciarConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    // Agendar consulta
    @PreAuthorize("hasRole('PACIENTE')")
    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@RequestBody GerenciarConsultaDTO dto) {
        var paciente = pacienteRepository.findById(dto.getPacienteId());
        var profissional = profissionalSaudeRepository.findById(dto.getProfissionalId());

        if (paciente.isEmpty())
            return ResponseEntity.badRequest().body("Paciente não encontrado.");
        if (profissional.isEmpty())
            return ResponseEntity.badRequest().body("Profissional não encontrado.");

        if (dto.getDataHoraConsulta() == null || dto.getMotivoConsulta() == null) {
            return ResponseEntity.badRequest().body("Data e motivo da consulta são obrigatórios.");
        }

        if (dto.getDataHoraConsulta().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Data da consulta não pode ser no passado.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente.get());
        consulta.setProfissionalSaude(profissional.get());
        consulta.setDataHora(dto.getDataHoraConsulta());
        consulta.setMotivoConsulta(dto.getMotivoConsulta());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    // Remarcar consulta
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @PutMapping("/remarcar")
    public ResponseEntity<?> remarcar(@RequestBody GerenciarConsultaDTO dto) {
        var consultaOpt = consultaRepository.findById(dto.getConsultaId());
        if (consultaOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var consulta = consultaOpt.get();
        consulta.setDataHora(dto.getDataHoraConsulta());
        consulta.setMotivoConsulta(dto.getMotivoConsulta());
        consulta.setStatus(StatusConsulta.REMARCADA);

        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    // Cancelar consulta
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        var consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var consulta = consultaOpt.get();
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);

        return ResponseEntity.ok("Consulta cancelada com sucesso.");
    }

    // Marcar como realizada
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @PutMapping("/realizada/{id}")
    public ResponseEntity<?> marcarComoRealizada(@PathVariable Long id) {
        var consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var consulta = consultaOpt.get();
        consulta.setStatus(StatusConsulta.REALIZADA);
        consultaRepository.save(consulta);

        return ResponseEntity.ok("Consulta marcada como realizada.");
    }

    // Histórico por paciente
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @GetMapping("/historico/{pacienteId}")
    public ResponseEntity<?> historico(@PathVariable Long pacienteId) {
        var consultas = consultaRepository.findByPacienteId(pacienteId);
        return ResponseEntity.ok(consultas);
    }
}
