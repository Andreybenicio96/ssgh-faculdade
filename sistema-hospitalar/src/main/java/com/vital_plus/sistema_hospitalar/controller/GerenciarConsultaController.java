package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.GerenciarConsultaDTO;
import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;
import com.vital_plus.sistema_hospitalar.model.Consulta;
import com.vital_plus.sistema_hospitalar.repository.ConsultaRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gerenciar-consultas")
@Tag(name = "Gerenciar Consultas", description = "Gerenciamento de Consultas Médicas")
@Slf4j
public class GerenciarConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    // Agendar consulta
    @Operation(summary = "Agendar Consulta", description = "Agenda uma nova consulta médica para um paciente com um profissional de saúde.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@Valid @RequestBody GerenciarConsultaDTO dto) {
        var paciente = pacienteRepository.findById(dto.getPacienteId());
        var profissional = profissionalSaudeRepository.findById(dto.getProfissionalId());

        if (paciente.isEmpty()){
            log.warn("Paciente não encontrado: {}", dto.getPacienteId());
            return ResponseEntity.badRequest().body("Paciente não encontrado.");
        }
        if (profissional.isEmpty()){
            log.warn("Profissional de saúde não encontrado: {}", dto.getProfissionalId());
            return ResponseEntity.badRequest().body("Profissional não encontrado.");
        }

        if (dto.getDataHoraConsulta() == null || dto.getMotivoConsulta() == null) {
            log.warn("Dados obrigatórios não informados: dataHoraConsulta={}, motivoConsulta={}", dto.getDataHoraConsulta(), dto.getMotivoConsulta());
            return ResponseEntity.badRequest().body("Data e motivo da consulta são obrigatórios.");
        }

        if (dto.getDataHoraConsulta().isBefore(java.time.LocalDateTime.now())) {
            log.warn("Tentativa de agendar consulta no passado: {}", dto.getDataHoraConsulta());
            return ResponseEntity.badRequest().body("Data da consulta não pode ser no passado.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente.get());
        consulta.setProfissionalSaude(profissional.get());
        consulta.setDataHora(dto.getDataHoraConsulta());
        consulta.setMotivoConsulta(dto.getMotivoConsulta());
        consulta.setStatus(StatusConsulta.AGENDADA);
        log.info("Agendando consulta: {}", consulta);
        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    // Remarcar consulta
    @Operation(summary = "Remarcar Consulta", description = "Altera a data e o motivo de uma consulta já agendada.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @PutMapping("/remarcar/{consultaId}")
    public ResponseEntity<?> remarcar(@Valid @RequestBody GerenciarConsultaDTO dto) {
        var consultaOpt = consultaRepository.findById(dto.getConsultaId());
        if (consultaOpt.isEmpty()) {
            log.warn("Consulta não encontrada: {}", dto.getConsultaId());
            return ResponseEntity.notFound().build();
        }
        var consulta = consultaOpt.get();
        consulta.setDataHora(dto.getDataHoraConsulta());
        consulta.setMotivoConsulta(dto.getMotivoConsulta());
        consulta.setStatus(StatusConsulta.REMARCADA);
        log.info("Remarcando consulta: {}", consulta);
        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    // Cancelar consulta
    @Operation(summary = "Cancelar Consaulta", description = "Cancela uma consulta agendada.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        var consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isEmpty()){
            log.warn("Consulta não encontrada para cancelamento: {}", id);
            return ResponseEntity.notFound().build();
        }    
        var consulta = consultaOpt.get();
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
        log.info("Consulta cancelada com sucesso: {}", consulta);
        return ResponseEntity.ok("Consulta cancelada com sucesso.");
    }

    // Marcar como realizada
    @Operation(summary = "Marcar Consulta como Realizada", description = "Marca uma consulta agendada como realizada.")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @PutMapping("/realizada/{id}")
    public ResponseEntity<?> marcarComoRealizada(@PathVariable Long id) {
        var consultaOpt = consultaRepository.findById(id);
        if (consultaOpt.isEmpty()){
            log.warn("Consulta não encontrada para marcar como realizada: {}", id);
            return ResponseEntity.notFound().build();
        }    
        var consulta = consultaOpt.get();
        consulta.setStatus(StatusConsulta.REALIZADA);
        consultaRepository.save(consulta);
        log.info("Consulta marcada como realizada: {}", consulta);
        return ResponseEntity.ok("Consulta marcada como realizada.");
    }

    // Histórico por paciente
    @Operation(summary = "Histórico de Consultas do Paciente", description = "Lista todas as consultas agendadas para um paciente especifico.")
    @PreAuthorize("hasRole('PACIENTE') or hasRole('PROFISSIONAL_SAUDE')")
    @GetMapping("/historico/{pacienteId}")
    public ResponseEntity<?> historico(@PathVariable Long pacienteId) {
        var consultas = consultaRepository.findByPacienteId(pacienteId);
        log.info("Consultas encontradas para o paciente ID {}: {}", pacienteId, consultas.size());
        return ResponseEntity.ok(consultas);
    }
}
