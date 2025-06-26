package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.NotificacaoDTO;
import com.vital_plus.sistema_hospitalar.model.Notificacao;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.NotificacoesRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@PreAuthorize("hasRole('ADMIN') or hasRole('PROFISSIONAL_SAUDE')")
public class NotificacoesController {

    @Autowired
    private NotificacoesRepository notificacoesRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Buscar todas as notificações de um paciente.
     */
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvo);
    }

    /**
     * Marcar uma notificação como lida.
     */
    @PutMapping("/marcar-lida/{id}")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable Long id) {
        Notificacao n = notificacoesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

        if (!n.isLida()) {
            n.setLida(true);
            n = notificacoesRepository.save(n);
        }
        return ResponseEntity.ok(n);
    }
}
