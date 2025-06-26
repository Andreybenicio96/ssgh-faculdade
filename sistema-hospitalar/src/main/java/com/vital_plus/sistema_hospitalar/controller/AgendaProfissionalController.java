package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.AgendaProfissional;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.AgendaProfissionalRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import org.springframework.http.*;


@RestController
@RequestMapping("/agenda-profissional")
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
    @PostMapping("/{profissionalId}")
        public ResponseEntity<AgendaProfissional> salvar(@PathVariable Long profissionalId, @RequestBody AgendaProfissional agenda) {
            Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
            if (profissional.isPresent()) {
                agenda.setProfissionalSaude(profissional.get());
                AgendaProfissional salvo = agendaProfissionalRepository.save(agenda);
                return ResponseEntity.ok(salvo);
            } else {
                return ResponseEntity.notFound().build();
            }
}


    /**
     * Endpoint para listar todas as agendas profissionais.
     * @return Lista de agendas profissionais
     */
    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<AgendaProfissional>> listarPorProfissional(@PathVariable Long profissionalId) {
        Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
        if (profissional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AgendaProfissional> agendas = agendaProfissionalRepository.findByProfissionalSaudeId(profissionalId);
        return ResponseEntity.ok(agendas);
    }

    /**
     * Endpoint para atualizar uma agenda profissional.
     * @param id ID da agenda profissional a ser atualizada
     * @param agenda Dados atualizados da agenda profissional
     * @return Agenda profissional atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgendaProfissional> atualizar(@PathVariable Long id, @RequestBody AgendaProfissional agenda) {
        Optional<AgendaProfissional> agendaExistente = agendaProfissionalRepository.findById(id);
        if (agendaExistente.isPresent()) {
            agenda.setId(id);
            AgendaProfissional atualizado = agendaProfissionalRepository.save(agenda);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
