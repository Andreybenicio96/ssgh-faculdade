package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.AgendaProfissional;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.AgendaProfissionalRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import org.springframework.http.*;


@RestController
@RequestMapping("/agenda-profissional")
@Tag(name = "Agenda Profissional", description = "Gerenciamento de Agendas Profissionais")
@Slf4j
public class AgendaProfissionalController {

    @Autowired
    private AgendaProfissionalRepository agendaProfissionalRepository;
    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    /**
     * Endpoint para cadastrar uma agenda profissional.
     * @param agendaProfissional Dados da agenda profissional a ser cadastrada
     * @return Agenda profissional salva
     */

    @Operation(summary = "Cadastrar Agenda Profissional", description = "Cadastra uma nova agenda profissional.") 
    @PostMapping("/{profissionalId}")
        public ResponseEntity<AgendaProfissional> salvar(@PathVariable Long profissionalId, @Valid @RequestBody AgendaProfissional agenda) {
            Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
            if (profissional.isPresent()) {
                agenda.setProfissionalSaude(profissional.get());
                AgendaProfissional salvo = agendaProfissionalRepository.save(agenda);
                log.info("Agenda cadastrada com sucesso para o profissional ID: {}");
                return ResponseEntity.ok(salvo);
            } else {
                log.warn("Profissional não encontrado para o ID: {}", profissionalId);
                return ResponseEntity.notFound().build();
            }
}


    /**
     * Endpoint para listar todas as agendas profissionais.
     * @return Lista de agendas profissionais
     */
    @Operation(summary = "Listar Agendas Profissionais", description = "Lista todas as agendas profissionais cadastradas.")
    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<AgendaProfissional>> listarPorProfissional(@PathVariable Long profissionalId) {
        Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
        if (profissional.isEmpty()) {
            log.warn("Profissional não encontrado para o ID: {}",profissionalId);
            return ResponseEntity.notFound().build();
        }

        List<AgendaProfissional> agendas = agendaProfissionalRepository.findByProfissionalSaudeId(profissionalId);
        log.info("Listando agendas para o profissional ID: {}", profissionalId);
        return ResponseEntity.ok(agendas);
    }

    /**
     * Endpoint para atualizar uma agenda profissional.
     * @param id ID da agenda profissional a ser atualizada
     * @param agenda Dados atualizados da agenda profissional
     * @return Agenda profissional atualizada
     */
    @Operation(summary = "Atualizar Agenda Profissional", description = "Atualiza uma agenda profissional existente.")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AgendaProfissional> atualizar(@PathVariable Long id, @Valid @RequestBody AgendaProfissional agenda) {
        Optional<AgendaProfissional> agendaExistente = agendaProfissionalRepository.findById(id);
        if (agendaExistente.isPresent()) {
            agenda.setId(id);
            AgendaProfissional atualizado = agendaProfissionalRepository.save(agenda);
            log.info("Agenda proffissional atualizada com sucesso: {}", id);
            return ResponseEntity.ok(atualizado);
        } else {
            log.warn("Agenda profissional não encontrada para o ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
