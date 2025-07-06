package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.ReceitaMedicaDTO;
import com.vital_plus.sistema_hospitalar.model.ReceitaMedica;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import com.vital_plus.sistema_hospitalar.repository.ReceitaMedicaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.vital_plus.sistema_hospitalar.configuracao.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/receitas")
@Tag(name = "Receitas Médicas", description = "Gerenciamento de Receitas Médicas")
@Slf4j
public class ReceitaMedicaController {

    @Autowired
    private ReceitaMedicaRepository receitaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;
    @Autowired
    private JwtService jwtService;


    /**
     * Emitir uma receita médica para um paciente.
     */
    @Operation(summary = "Emitir Receita Médica", description = "Emite uma nova receita médica para um paciente.")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @PostMapping("/emitir")
    public ResponseEntity<?> emitirReceita(@Valid @RequestBody ReceitaMedicaDTO dto) {
        var paciente = pacienteRepository.findById(dto.getPacienteId());
        var profissional = profissionalRepository.findById(dto.getProfissionalId());

        if (paciente.isEmpty() || profissional.isEmpty()) {
            log.warn("Tentativa de emissão de receita com paciente ou profissional não encontrado: pacienteId={}, profissionalId={}", dto.getPacienteId(), dto.getProfissionalId());
            return ResponseEntity.badRequest().body("Paciente ou Profissional não encontrado.");
        }
        LocalDate dataEmissao = dto.getDataEmissao();
        if (dataEmissao == null) {
            log.warn("Tentativa de emissão de receita sem data de emissão: {}", dto);
            return ResponseEntity.badRequest().body("A data de emissão é obrigatória.");
        }

        if (dataEmissao.isAfter(LocalDate.now())) {
            log.warn("Tentativa de emissão de receita com data futura: dataEmissao={}", dto.getDataEmissao());
            return ResponseEntity.badRequest().body("A data de emissão não pode ser no futuro.");
        }
        if (dataEmissao.isBefore(LocalDate.of(1900, 1, 1))) {
            log.warn("Tentativa de emissão de receita com data inválida: dataEmissao={}", dto.getDataEmissao());
            return ResponseEntity.badRequest().body("A data de emissão é inválida.");
        }

        ReceitaMedica receita = ReceitaMedica.builder()
                .paciente(paciente.get())
                .profissionalSaude(profissional.get())
                .conteudo(dto.getConteudo())
                .dataEmissao(LocalDate.now())
                .build();
        log.info("Emitindo receita médica: {}", receita);
        return ResponseEntity.ok(receitaRepository.save(receita));
    }
    /**
     * Listar todas as receitas médicas.
     */
    @Operation(summary = "Listar Receitas Médicas", description = "Lista todas as receitas médicas emitidas.")
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}")
    public ResponseEntity<?> listarPorPaciente(@PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extrairEmail(token);

        var pacienteOpt = pacienteRepository.findByEmail(email);
        if (pacienteOpt.isEmpty()) {
            log.warn("Paciente não encontrado com email: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Paciente não encontrado.");
        }


        var paciente = pacienteOpt.get();
        if (!paciente.getId().equals(id)) {
            log.warn("Tentativa de acesso não autorizado às receitas de outro paciente: pacienteId={}, email={}", id, email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para ver as receitas de outro paciente.");
        }
        log.info("Listando Receitas Médicas para o paciente ID: {}", id);
        return ResponseEntity.ok(receitaRepository.findByPacienteId(id));
    }
    /**
     * Listar receitas médicas por profissional de saúde.
     */
    @Operation(summary = "Listar Receitas Médicas por Profissional de Saúde", description = "Lista todas as receitas médicas emitidas por um profissional de saúde específico.")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @GetMapping("/profissional/{id}")
    public ResponseEntity<List<ReceitaMedica>> listarPorProfissional(@PathVariable Long id) {
        log.info("Listando Receitas Médicas para o profissional de saúde ID: {}", id);
        if (receitaRepository.findByProfissionalSaudeId(id).isEmpty()) {
            log.warn("Nenhuma receita encontrada para o profissional de saúde ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receitaRepository.findByProfissionalSaudeId(id));
    }
}
