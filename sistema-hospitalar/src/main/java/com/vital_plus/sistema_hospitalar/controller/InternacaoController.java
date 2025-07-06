package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.InternacaoDTO;
import com.vital_plus.sistema_hospitalar.model.Internacao;
import com.vital_plus.sistema_hospitalar.model.Leito;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.InternacaoRepository;
import com.vital_plus.sistema_hospitalar.repository.LeitoRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/internacoes")
@Tag(name = "Internações", description = "Gerenciamento de Internações Hospitalares")
@Slf4j
public class InternacaoController {

    @Autowired
    private InternacaoRepository internacaoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    /**
     * Internar paciente (associando com leito disponível).
     */
    @Operation(summary = "Internar Paciente", description = "Realiza a internação de um paciente em um leito disponível.")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFISSIONAL_SAUDE')")
    @PostMapping("/internar")
    public ResponseEntity<?> internar(@Valid @RequestBody InternacaoDTO dto) {
        var pacienteOpt = pacienteRepository.findById(dto.getPacienteId());
        var leitoOpt = leitoRepository.findById(dto.getLeitoId());

        if (pacienteOpt.isEmpty()) {
            log.warn("Paciente não encontrado: {}", dto.getPacienteId());
            return ResponseEntity.badRequest().body("Paciente não encontrado.");
        }

        if (leitoOpt.isEmpty()) {
            log.warn("Leito não encontrado: {}", dto.getLeitoId());
            return ResponseEntity.badRequest().body("Leito não encontrado.");
        }

        Leito leito = leitoOpt.get();
        if (leito.isOcupado()) {
            log.warn("Leito já está ocupado: {}", leito.getId());
            return ResponseEntity.badRequest().body("Leito já está ocupado.");
        }

        // Atualizar o leito com ocupação
        leito.setOcupado(true);
        leito.setPaciente(pacienteOpt.get());
        leito.setDataHoraOcupacao(LocalDateTime.now());
        leitoRepository.save(leito);

        // Criar a internação
        Internacao internacao = new Internacao();
        internacao.setPacienteId(pacienteOpt.get());
        internacao.setLeito(leito);
        internacao.setQuarto(dto.getQuarto());
        internacao.setMotivoInternacao(dto.getMotivoInternacao());
        internacao.setDataHoraEntrada(dto.getDataHoraEntrada());

        Internacao salva = internacaoRepository.save(internacao);
        log.info("Internação realizada com sucesso: {}", salva.getId());
        return ResponseEntity.ok(salva);
    }

    /**
     * Dar alta ao paciente, liberando leito.
     */
    @Operation(summary = "Dar Alta ao Paciente", description = "Realiza a alta de um paciente internado, liberando o leito.")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFISSIONAL_SAUDE')")
    @PutMapping("/alta/{id}")
    public ResponseEntity<?> darAlta(@PathVariable Long id) {
        var internacaoOpt = internacaoRepository.findById(id);
        if (internacaoOpt.isEmpty()) {
            log.warn("Internação não encontrada: {}", id);
            return ResponseEntity.notFound().build();
        }

        var internacao = internacaoOpt.get();
        if (internacao.getDataHoraSaida() != null) {
            log.warn("Paciente já recebeu alta: {}", internacao.getPacienteId().getId());
            return ResponseEntity.badRequest().body("Paciente já recebeu alta.");
        }

        // Atualiza alta
        internacao.setDataHoraSaida(LocalDateTime.now());
        internacaoRepository.save(internacao);

        // Libera leito
        Leito leito = internacao.getLeito();
        leito.setOcupado(false);
        leito.setPaciente(null);
        leito.setDataHoraOcupacao(null);
        leitoRepository.save(leito);

        log.info("Alta realizada. Leito liberado: {}", leito.getId());
        return ResponseEntity.ok("Alta realizada com sucesso.");
    }

    /**
     * Listar todas internações.
     */
    @Operation(summary = "Listar Internações", description = "Lista todas as internações hospitalares registradas.")
    @GetMapping
    public ResponseEntity<List<Internacao>> listarInternacoes() {
        log.info("Listando todas as internações.");
        return ResponseEntity.ok(internacaoRepository.findAll());
    }

    /**
     * Buscar uma internação por ID.
     */
    @Operation(summary = "Buscar Internação por ID", description = "Busca uma internação específica pelo seu ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Internacao> buscarPorId(@PathVariable Long id) {
        log.info("Buscando internação por ID: {}", id);
        return internacaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Excluir uma internação.
     */
    @Operation(summary = "Excluir Internação", description = "Exclui uma internação pelo seu ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!internacaoRepository.existsById(id)) {
            log.warn("Internação não encontrada para exclusão: {}", id);
            return ResponseEntity.notFound().build();
        }
        internacaoRepository.deleteById(id);
        log.info("Internação excluída com sucesso: {}", id);
        return ResponseEntity.noContent().build();
    }
}
