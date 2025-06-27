package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.LoginRequest;
import com.vital_plus.sistema_hospitalar.dto.LoginResponse;
import com.vital_plus.sistema_hospitalar.model.Administrador;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.AdministradorRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import com.vital_plus.sistema_hospitalar.configuracao.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class AutenticadorController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        String email = login.getEmail();
        String senha = login.getSenha();

        // Tenta autenticar como Paciente
        var paciente = pacienteRepository.findByEmail(email);
        if (paciente.isPresent() && passwordEncoder.matches(senha, paciente.get().getSenhaHash())) {
            String token = jwtService.gerarToken(paciente.get().getEmail(), "PACIENTE");
            return ResponseEntity.ok(new LoginResponse(token, "PACIENTE"));
        }

        // Tenta autenticar como Profissional
        var profissional = profissionalRepository.findByEmail(email);
        if (profissional.isPresent() && passwordEncoder.matches(senha, profissional.get().getSenhaHash())) {
            String token = jwtService.gerarToken(profissional.get().getEmail(), "PROFISSIONAL_SAUDE");
            return ResponseEntity.ok(new LoginResponse(token, "PROFISSIONAL_SAUDE"));
        }

        // Tenta autenticar como Administrador
        var administrador = administradorRepository.findByEmail(email);
        if (administrador.isPresent() && passwordEncoder.matches(senha, administrador.get().getSenhaHash())) {
            String token = jwtService.gerarToken(administrador.get().getEmail(), "ADMIN");
            return ResponseEntity.ok(new LoginResponse(token, "ADMIN"));
        }

        return ResponseEntity.status(401).body("Email ou senha inválidos.");
    }
}
