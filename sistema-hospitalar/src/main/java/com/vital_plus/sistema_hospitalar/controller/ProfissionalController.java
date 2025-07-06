package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.AtualizarPerfilProfissionalDTO;
import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
@Tag(name = "Profissionais de Saúde", description = "Gerenciamento de Profissionais de Saúde")
@Slf4j
public class ProfissionalController {

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

   
    @Operation(summary = "Atualizar Perfil Profissional", description = "Permite que um profissional de saúde atualize seu perfil.")
    @PutMapping("/atualizar-perfil/{id}")
public ResponseEntity<?> atualizarPerfil(@PathVariable Long id,
                                        @Valid @RequestBody AtualizarPerfilProfissionalDTO dto) {
    var profissionalOpt = profissionalRepository.findById(id);
    if (profissionalOpt.isEmpty()) return ResponseEntity.notFound().build();
    if (!profissionalOpt.get().getTipoUsuario().equals(TipoUsuario.PROFISSIONAL_SAUDE)) {
        log.warn("Tentativa de atualização de perfil por usuário não profissional: {}", profissionalOpt.get().getEmail());
        return ResponseEntity.status(403).body("Acesso negado. Apenas profissionais de saúde podem atualizar o perfil.");
    }
    var profissional = profissionalOpt.get();
    profissional.atualizarPerfilProfissional(
        dto.getNomeUsuario(),
        dto.getEmail(),
        dto.getTelefone(),
        dto.getEspecialidade(),
        dto.getRegistroProfissional()
    );

    profissionalRepository.save(profissional);
    log.info("Perfil do profissional atualizado com sucesso: {}", profissional.getEmail());
    return ResponseEntity.ok("Perfil do profissional atualizado com sucesso.");
}

    @Operation(summary = "Listar Profissionais de Saúde", description = "Lista todos os profissionais de saúde cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<?> listar() {
        log.info("Listando todos os profissionais de saúde.");
        return ResponseEntity.ok(profissionalRepository.findAll());
    }
}
