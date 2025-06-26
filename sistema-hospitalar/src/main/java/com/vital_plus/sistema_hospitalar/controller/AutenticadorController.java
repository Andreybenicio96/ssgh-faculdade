package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.vital_plus.sistema_hospitalar.dto.LoginRequest;
import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.repository.*;

@RestController
@RequestMapping("/autenticacao")
public class AutenticadorController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        var pacienteOpt = pacienteRepository.findByEmail(login.getEmail());

        if (pacienteOpt.isPresent()) {
            var paciente = pacienteOpt.get();
            if (passwordEncoder.matches(login.getSenha(), paciente.getSenhaHash())) {
                return ResponseEntity.ok(paciente); // ou gerar um token
            }
        }
        var adminOpt = administradorRepository.findByEmail(login.getEmail());
        if (adminOpt.isPresent()) {
            var admin = adminOpt.get();
            if (passwordEncoder.matches(login.getSenha(), admin.getSenhaHash())) {
                return ResponseEntity.ok(admin); // ou gerar um token
            }
        }
        var profissionalOpt = profissionalSaudeRepository.findByEmail(login.getEmail());
        if (profissionalOpt.isPresent()) {
            var profissional = profissionalOpt.get();
            if (passwordEncoder.matches(login.getSenha(), profissional.getSenhaHash())) {
                return ResponseEntity.ok(profissional); // ou gerar um token
            }
        }


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
    }
}
