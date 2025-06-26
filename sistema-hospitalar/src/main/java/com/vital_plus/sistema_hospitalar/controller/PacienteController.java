package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Paciente paciente) {
        if (paciente.getTipoUsuario() == null || !paciente.getTipoUsuario().equals(TipoUsuario.PACIENTE)) {
            return ResponseEntity.badRequest().body("Tipo de usuário inválido. Esperado: PACIENTE.");
        }
        if (paciente.getSenhaHash() == null || paciente.getSenhaHash().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha não pode ser vazia.");
        }
        if (paciente.getEmail() == null || paciente.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email não pode ser vazio.");
        }
        if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Já existe um paciente cadastrado com este email.");
        }
        if (paciente.getNomeUsuario() == null || paciente.getNomeUsuario().isEmpty()) {
            paciente.setNomeUsuario(paciente.getEmail().split("@")[0]);
        }
        if (paciente.getSenhaHash().length() < 6) {
            return ResponseEntity.badRequest().body("A senha deve ter pelo menos 6 caracteres.");
        }

        paciente.setSenhaHash(passwordEncoder.encode(paciente.getSenhaHash()));
        Paciente salvo = pacienteRepository.save(paciente);
        return ResponseEntity.ok(salvo);
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.atualizarPerfilPaciente(
                        pacienteAtualizado.getNomeUsuario(),
                        pacienteAtualizado.getEmail(),
                        pacienteAtualizado.getTelefone(),
                        pacienteAtualizado.getNomeCompleto(),
                        pacienteAtualizado.getEndereco(),
                        pacienteAtualizado.getDataNascimento(),
                        pacienteAtualizado.getSexo()
                    );
                    pacienteRepository.save(paciente);
                    return ResponseEntity.ok("Perfil atualizado com sucesso.");
                })
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping
    public ResponseEntity<Iterable<Paciente>> listar() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }
}
