package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.IniciarTeleconsultaDTO;
import com.vital_plus.sistema_hospitalar.model.Consulta;
import com.vital_plus.sistema_hospitalar.model.Teleconsulta;
import com.vital_plus.sistema_hospitalar.repository.ConsultaRepository;
import com.vital_plus.sistema_hospitalar.repository.TeleconsultaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/teleconsulta")
@Tag(name = "Teleconsultas", description = "Gerenciamento de Teleconsultas Médicas")
@Slf4j
public class TeleconsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TeleconsultaRepository teleconsultaRepository;

    /**
     * Iniciar uma teleconsulta para uma consulta existente.
     * O link de acesso é gerado automaticamente.
     */
    @Operation(summary = "Iniciar Teleconsulta", description = "Inicia uma teleconsulta para uma consulta existente, gerando um link de acesso.")
    @PreAuthorize("hasRole('ROLE_PROFISSIONAL_SAUDE')") 
    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciar(@RequestBody @Valid IniciarTeleconsultaDTO dto) {
        log.info("Iniciando teleconsulta para consulta ID {}", dto.getConsultaId());

        var consultaOpt = consultaRepository.findById(dto.getConsultaId());

        if (consultaOpt.isEmpty()) {
            log.warn("Consulta não encontrada para ID {}", dto.getConsultaId());
            return ResponseEntity.badRequest().body("Consulta não encontrada.");
        }

        if (teleconsultaRepository.findByConsultaId(dto.getConsultaId()).isPresent()) {
            log.warn("Teleconsulta já iniciada para consulta ID {}", dto.getConsultaId());
            return ResponseEntity.badRequest().body("Teleconsulta já iniciada para esta consulta.");
        }

        Consulta consulta = consultaOpt.get();
        Teleconsulta teleconsulta = Teleconsulta.builder()
                .consulta(consulta)
                .linkAcesso("https://meet.jit.si/vitalplus-consulta-" + consulta.getId())
                .dataHoraCriacao(LocalDateTime.now())
                .build();

        teleconsultaRepository.save(teleconsulta);
        log.info("Teleconsulta iniciada com sucesso para consulta ID {}", consulta.getId());
        return ResponseEntity.ok(teleconsulta);
    }
    /**
     * Buscar teleconsulta por ID da consulta.
     */
    @Operation(summary = "Buscar Teleconsulta por Consulta ID", description = "Retorna a teleconsulta associada a um ID de consulta especifico.")
    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<?> buscarPorConsulta(@PathVariable Long consultaId) {
        log.info("Buscando teleconsulta para consulta ID {}", consultaId);
        return teleconsultaRepository.findByConsultaId(consultaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Nenhuma teleconsulta encontrada para consulta ID {}", consultaId);
                    return ResponseEntity.notFound().build();
                });
    }
}
