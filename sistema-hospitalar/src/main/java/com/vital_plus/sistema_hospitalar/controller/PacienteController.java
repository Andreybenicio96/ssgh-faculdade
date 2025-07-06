package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Gerenciamento de Pacientes")
@Slf4j
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Operation(summary = "Atualizar Paciente", description = "Atualiza informações do paciente no sistema.")
    @PreAuthorize("hasRole('PACIENTE')")
    @PutMapping("/atualizar-perfil/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @Valid @RequestBody Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.atualizarPerfilPaciente(
                            pacienteAtualizado.getNomeUsuario(),
                            pacienteAtualizado.getEmail(),
                            pacienteAtualizado.getTelefone(),
                            pacienteAtualizado.getNomeCompleto(),
                            pacienteAtualizado.getEndereco(),
                            pacienteAtualizado.getDataNascimento(),
                            pacienteAtualizado.getSexo());
                    pacienteRepository.save(paciente);
                    log.info("Paciente atualizado com sucesso: {}", paciente.getEmail());
                    return ResponseEntity.ok("Perfil atualizado com sucesso.");
                })
                .orElseGet(() -> {
                    log.warn("Tentativa de atualizar paciente inexistente: ID {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    @Operation(summary = "Listar Pacientes", description = "Lista todos os pacientes cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<Iterable<Paciente>> listar() {
        log.info("Listando todos os pacientes");
        return ResponseEntity.ok(pacienteRepository.findAll());
    }
}
