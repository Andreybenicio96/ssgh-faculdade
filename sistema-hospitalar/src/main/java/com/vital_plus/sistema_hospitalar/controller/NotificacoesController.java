package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.NotificacaoDTO;
import com.vital_plus.sistema_hospitalar.model.Notificacao;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.NotificacoesRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Gerenciamento de Notificações para Pacientes")
@Slf4j
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFISSIONAL_SAUDE')")
public class NotificacoesController {

    @Autowired
    private NotificacoesRepository notificacoesRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Buscar todas as notificações de um paciente.
     */

    @Operation(summary = "Buscar Notificações por Paciente", description = "Retorna todas as notificações associadas a um paciente específico.")
    @GetMapping("/{pacienteId}")
    public ResponseEntity<List<Notificacao>> buscarPorPaciente(@PathVariable Long pacienteId) {
        // Valida existência do paciente
        pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        List<Notificacao> lista = notificacoesRepository.findByPacienteId(pacienteId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Criar nova notificação para um paciente.
     */
    @Operation(summary = "Criar Notificação", description = "Cria uma nova notificação para um paciente.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Notificacao> criar(@Valid @RequestBody NotificacaoDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente não encontrado"));

        Notificacao n = Notificacao.builder()
                .paciente(paciente)
                .mensagem(dto.getMensagem())
                .dataHora(dto.getDataHora() != null ? dto.getDataHora() : LocalDateTime.now())
                .lida(false)
                .build();

        Notificacao salvo = notificacoesRepository.save(n);
        log.info("Notificação criada com sucesso para o paciente ID: {}", paciente.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvo);
    }

    /**
     * Marcar uma notificação como lida.
     */
    @Operation(summary = "Marcar Notificação como Lida", description = "Marca uma notificação específica como lida.")
    @PutMapping("/marcar-lida/{id}")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable Long id) {
        Notificacao n = notificacoesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

        if (!n.isLida()) {
            n.setLida(true);
            n = notificacoesRepository.save(n);
        }
        log.info("Notificação marcada como lida: {}", n.getId());
        return ResponseEntity.ok(n);
    }
}
