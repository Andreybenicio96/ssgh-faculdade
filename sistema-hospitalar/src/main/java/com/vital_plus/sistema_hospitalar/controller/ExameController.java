package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.Exame;
import com.vital_plus.sistema_hospitalar.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.http.*;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exames")
public class ExameController {
    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    /**
     * Endpoint para cadastrar um exame.
     * @param pacienteId ID do paciente associado ao exame
     * @param profissionalId ID do profissional de saúde associado ao exame
     * @param exame Dados do exame a ser cadastrado
     * @return Exame salvo
     */
    @PostMapping("/{pacienteId}/{profissionalId}")
    public ResponseEntity<Exame> salvar(@PathVariable Long pacienteId, @PathVariable Long profissionalId, @RequestBody Exame exame) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        Optional<ProfissionalSaude> profissional = profissionalSaudeRepository.findById(profissionalId);
        if (paciente.isPresent() && profissional.isPresent()) {
            exame.setPaciente(paciente.get());
            exame.setProfissionalSaude(profissional.get());
            Exame salvo = exameRepository.save(exame);
            return ResponseEntity.ok(salvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para listar todos os exames.
     * @return Lista de exames
     */
    @GetMapping
    public ResponseEntity<List<Exame>> listar() {
        List<Exame> exames = exameRepository.findAll();
        return ResponseEntity.ok(exames);
    }
}
