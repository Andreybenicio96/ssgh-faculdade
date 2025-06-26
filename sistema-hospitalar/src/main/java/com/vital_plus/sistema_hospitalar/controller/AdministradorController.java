package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.AtualizarPerfilAdministradorDTO;
import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.Administrador;
import com.vital_plus.sistema_hospitalar.model.Internacao;
import com.vital_plus.sistema_hospitalar.repository.AdministradorRepository;
import com.vital_plus.sistema_hospitalar.repository.InternacaoRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalSaudeRepository;

    @Autowired
    private InternacaoRepository internacaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint para cadastrar um novo administrador.
     */
    @PostMapping
    public ResponseEntity<Administrador> salvar(@RequestBody Administrador admin) {
        // Valida se o tipo de usuário está correto para administrador
        if (admin.getTipoUsuario() == null || !admin.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
            return ResponseEntity.badRequest().body(null);
        }
        // Valida e exige e-mail e senha
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (admin.getSenhaHash() == null || admin.getSenhaHash().isEmpty() || admin.getSenhaHash().length() < 6) {
            return ResponseEntity.badRequest().body(null);
        }
        // Verifica duplicação de e-mail
        if (administradorRepository.findByEmail(admin.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        // Define nome de usuário padrão, se não informado
        if (admin.getNomeUsuario() == null || admin.getNomeUsuario().isEmpty()) {
            admin.setNomeUsuario(admin.getEmail().split("@")[0]);
        }

        // Criptografa a senha
        admin.setSenhaHash(passwordEncoder.encode(admin.getSenhaHash()));
        Administrador salvo = administradorRepository.save(admin);
        return ResponseEntity.ok(salvo);

    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar-profissional")
    public ResponseEntity<?> cadastrarProfissional(@RequestBody Administrador admin) {
        // Valida se o tipo de usuário está correto para profissional de saúde
        if (admin.getTipoUsuario() == null || !admin.getTipoUsuario().equals(TipoUsuario.PROFISSIONAL_SAUDE)) {
            return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
        }
        // Valida e exige e-mail e senha
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }
        if (admin.getSenhaHash() == null || admin.getSenhaHash().isEmpty() || admin.getSenhaHash().length() < 6) {
            return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres.");
        }
        // Verifica duplicação de e-mail
        if (administradorRepository.findByEmail(admin.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }
        // Define nome de usuário padrão, se não informado
        if (admin.getNomeUsuario() == null || admin.getNomeUsuario().isEmpty()) {
            admin.setNomeUsuario(admin.getEmail().split("@")[0]);
        }

        // Criptografa a senha
        admin.setSenhaHash(passwordEncoder.encode(admin.getSenhaHash()));
        Administrador salvo = administradorRepository.save(admin);
        return ResponseEntity.ok(salvo);
    }
    /**
     * Atualizar perfil do administrador.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id,
            @RequestBody AtualizarPerfilAdministradorDTO dto) {
        var adminOpt = administradorRepository.findById(id);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var admin = adminOpt.get();
        // O método atualizarPerfilAdministrador deve estar implementado na entidade
        // Administrador,
        // atualizando os campos desejados (ex: nomeUsuario, email, telefone,
        // departamento).
        admin.atualizarPerfilAdministrador(
                dto.getNomeUsuario(),
                dto.getEmail(),
                dto.getTelefone(),
                dto.getDepartamento());
        administradorRepository.save(admin);
        return ResponseEntity.ok("Perfil do administrador atualizado com sucesso.");
    }

    /**
     * Listar todos os administradores.
     */
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(administradorRepository.findAll());
    }

    /**
     * Endpoint para remover um paciente (exemplo de gerenciamento de cadastros).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remover-paciente/{id}")
    public ResponseEntity<?> removerPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        return ResponseEntity.ok("Paciente removido com sucesso.");
    }

    /**
     * Endpoint para remover um profissional de saúde.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remover-profissional/{id}")
    public ResponseEntity<?> removerProfissional(@PathVariable Long id) {
        profissionalSaudeRepository.deleteById(id);
        return ResponseEntity.ok("Profissional de saúde removido com sucesso.");
    }

    /**
     * Endpoint para gerar um relatório geral de cadastros.
     */
    @GetMapping("/relatorio")
    public ResponseEntity<?> gerarRelatorioGeral() {
        Map<String, Long> relatorio = new HashMap<>();
        relatorio.put("Total de Pacientes", pacienteRepository.count());
        relatorio.put("Total de Profissionais de Saúde", profissionalSaudeRepository.count());
        relatorio.put("Total de Administradores", (long) administradorRepository.findAll().size());
        return ResponseEntity.ok(relatorio);
    }

    /**
     * Endpoint para visualizar todas as internações.
     */
    @GetMapping("/internacoes")
    public ResponseEntity<?> listarInternacoes() {
        return ResponseEntity.ok(internacaoRepository.findAll());
    }

    /**
     * Endpoint para visualizar internações ativas (sem alta)
     */
    @GetMapping("/internacoes-ativas")
    public ResponseEntity<?> listarInternacoesAtivas() {
        List<Internacao> ativas = internacaoRepository.findAll()
                .stream()
                .filter(internacao -> internacao.getDataHoraSaida() == null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ativas);
    }
}
