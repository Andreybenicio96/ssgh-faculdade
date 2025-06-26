package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.AtualizarPerfilProfissionalDTO;
import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ProfissionalSaude> salvar(@RequestBody ProfissionalSaude profissional) {
        if (profissional.getTipoUsuario() == null || !profissional.getTipoUsuario().equals(TipoUsuario.PROFISSIONAL_SAUDE)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (profissional.getSenhaHash() == null || profissional.getSenhaHash().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (profissional.getEmail() == null || profissional.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (profissionalRepository.findByEmail(profissional.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (profissional.getNomeUsuario() == null || profissional.getNomeUsuario().isEmpty()) {
            profissional.setNomeUsuario(profissional.getEmail().split("@")[0]);
        }
        if (profissional.getSenhaHash().length() < 6) {
            return ResponseEntity.badRequest().body(null);
        }


        profissional.setSenhaHash(passwordEncoder.encode(profissional.getSenhaHash()));
        return ResponseEntity.ok(profissionalRepository.save(profissional));
    }
    
    @PutMapping("/{id}")
public ResponseEntity<?> atualizarPerfil(@PathVariable Long id,
                                         @RequestBody AtualizarPerfilProfissionalDTO dto) {
    var profissionalOpt = profissionalRepository.findById(id);
    if (profissionalOpt.isEmpty()) return ResponseEntity.notFound().build();

    var profissional = profissionalOpt.get();
    profissional.atualizarPerfilProfissional(
        dto.getNomeUsuario(),
        dto.getEmail(),
        dto.getTelefone(),
        dto.getEspecialidade(),
        dto.getRegistroProfissional()
    );

    profissionalRepository.save(profissional);
    return ResponseEntity.ok("Perfil do profissional atualizado com sucesso.");
}


    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(profissionalRepository.findAll());
    }
}
