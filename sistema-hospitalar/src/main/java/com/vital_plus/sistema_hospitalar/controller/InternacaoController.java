package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.InternacaoDTO;
import com.vital_plus.sistema_hospitalar.model.Internacao;
import com.vital_plus.sistema_hospitalar.model.Leito;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.InternacaoRepository;
import com.vital_plus.sistema_hospitalar.repository.LeitoRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/internacoes")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFISSIONAL_SAUDE')")
    @PostMapping("/internar")
    public ResponseEntity<?> internar(@RequestBody InternacaoDTO dto) {
        var pacienteOpt = pacienteRepository.findById(dto.getPacienteId());
        var leitoOpt = leitoRepository.findById(dto.getLeitoId());

        if (pacienteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Paciente não encontrado.");
        }

        if (leitoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Leito não encontrado.");
        }

        Leito leito = leitoOpt.get();
        if (leito.isOcupado()) {
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
        internacao
                .setDataHoraEntrada(dto.getDataHoraEntrada() != null ? dto.getDataHoraEntrada() : LocalDateTime.now());
        internacao.setMotivoInternacao(dto.getMotivoInternacao());

        Internacao salva = internacaoRepository.save(internacao);
        return ResponseEntity.ok(salva);
    }

    /**
     * Dar alta ao paciente, liberando leito.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFISSIONAL_SAUDE')")
    @PutMapping("/alta/{id}")
    public ResponseEntity<?> darAlta(@PathVariable Long id) {
        var internacaoOpt = internacaoRepository.findById(id);
        if (internacaoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var internacao = internacaoOpt.get();
        if (internacao.getDataHoraSaida() != null) {
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

        return ResponseEntity.ok("Alta realizada com sucesso.");
    }

    /**
     * Listar todas internações.
     */
    @GetMapping
    public ResponseEntity<List<Internacao>> listarInternacoes() {
        return ResponseEntity.ok(internacaoRepository.findAll());
    }

    /**
     * Buscar uma internação por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Internacao> buscarPorId(@PathVariable Long id) {
        return internacaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Excluir uma internação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!internacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        internacaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
