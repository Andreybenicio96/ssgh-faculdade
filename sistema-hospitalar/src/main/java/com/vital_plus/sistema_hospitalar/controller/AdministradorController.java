package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.AtualizarPerfilAdministradorDTO;
import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import com.vital_plus.sistema_hospitalar.model.Administrador;
import com.vital_plus.sistema_hospitalar.model.Internacao;
import com.vital_plus.sistema_hospitalar.model.Paciente;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import com.vital_plus.sistema_hospitalar.repository.AdministradorRepository;
import com.vital_plus.sistema_hospitalar.repository.InternacaoRepository;
import com.vital_plus.sistema_hospitalar.repository.PacienteRepository;
import com.vital_plus.sistema_hospitalar.repository.ProfissionalSaudeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
@Tag(name = "Administradores", description = "Gerenciamento de Administradores")
@Slf4j
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
    @Operation(summary = "Cadastrar Administrador", description = "Cadastra um novo administrador no sistema.")
    @PostMapping("/cadastrar-administrador")
    public ResponseEntity<?> cadastrarAdministrador(@Valid @RequestBody Administrador admin) {
        // Valida se o tipo de usuário está correto para administrador
        if (admin.getTipoUsuario() == null || !admin.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
            log.warn("Tentativa de cadastro com tipo de usuario inválido:{}", admin.getTipoUsuario());
            return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
        }
        // Valida e exige e-mail e senha
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            log.warn("E-mail não informado para cadastro de administrador.");
            return ResponseEntity.badRequest().body("Email é obrigatorio.");
        }
        if (admin.getSenhaHash() == null || admin.getSenhaHash().isEmpty() || admin.getSenhaHash().length() < 6) {
            log.warn("Senha inválida para cadastro de administrador.");
            return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres.");
        }
        // Verifica duplicação de e-mail
        if (administradorRepository.findByEmail(admin.getEmail()).isPresent()) {
            log.warn("E-mail já cadastrado: {}", admin.getEmail());
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        if(admin.getSenhaHash() == null || admin.getSenhaHash().isEmpty()){
            log.warn("Senha não informada para cadastro");
            return ResponseEntity.badRequest().body(null);
            
        }
        // Define nome de usuário padrão, se não informado
        if (admin.getNomeUsuario() == null || admin.getNomeUsuario().isEmpty()) {
            log.info("Nome de usuário não informado, usando parte do email como padrão.");
            admin.setNomeUsuario(admin.getEmail().split("@")[0]);
        }
        

        // Criptografa a senha
        admin.setSenhaHash(passwordEncoder.encode(admin.getSenhaHash()));
        Administrador salvo = administradorRepository.save(admin);
        return ResponseEntity.ok(salvo);

    }
    @Operation(summary = "Cadastrar Paciente", description = "Cadastra um novo paciente no sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar-paciente")
    public ResponseEntity<?> cadastrarPaciente(@Valid @RequestBody Paciente paciente) {
        // Valida se o tipo de usuário está correto para paciente
        if (paciente.getTipoUsuario() == null || !paciente.getTipoUsuario().equals(TipoUsuario.PACIENTE)) {
            log.warn("Tentativa de cadastro com tipo de usuario inválido:{}", paciente.getTipoUsuario());
            return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
        }
        // Valida e exige e-mail e senha
        if (paciente.getEmail() == null || paciente.getEmail().isEmpty()) {
            log.warn("Email não informado para cadastro de paciente.");
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }
        if (paciente.getSenhaHash() == null || paciente.getSenhaHash().isEmpty() || paciente.getSenhaHash().length() < 6) {
            log.warn("Senha inválida para cadastro de paciente.");
            return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres.");
        }
        // Verifica duplicação de e-mail
        if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
            log.warn("Email já cadastrado: {}", paciente.getEmail());
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }
        // Define nome de usuário padrão, se não informado
        if (paciente.getNomeUsuario() == null || paciente.getNomeUsuario().isEmpty()) {
            log.info("Nome de usuário não informado, usando parte do email como padrão.");
            paciente.setNomeUsuario(paciente.getEmail().split("@")[0]);
        }

        // Criptografa a senha
        paciente.setSenhaHash(passwordEncoder.encode(paciente.getSenhaHash()));
        Paciente salvo = pacienteRepository.save(paciente);
        log.info("Paciente cadastrdo com sucesso: {}", paciente.getNomeUsuario());
        return ResponseEntity.ok(salvo);

    }
    
    @Operation(summary = "Cadastrar Profissional de saúde", description = "Cadastra um novo profissional de saúde no sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar-profissional")
    public ResponseEntity<?> salvar(@Valid @RequestBody ProfissionalSaude profissional) {
        // Valida se o tipo de usuário está correto para profissional de saúde
        if (profissional.getTipoUsuario() == null || !profissional.getTipoUsuario().equals(TipoUsuario.PROFISSIONAL_SAUDE)) {
            log.warn("Tipo de usuário inválido: {}", profissional.getTipoUsuario());
            return ResponseEntity.badRequest().body("Tipo de usuário inválido.");
        }
        // Valida e exige e-mail e senha
        if (profissional.getEmail() == null || profissional.getEmail().isEmpty()) {
            log.warn("Email não informado para cadastro de profissional de saúde.");
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }
        if (profissional.getSenhaHash() == null || profissional.getSenhaHash().isEmpty() || profissional.getSenhaHash().length() < 6) {
            log.warn("Senha inválida para cadastro de profissional de saúde.");
            return ResponseEntity.badRequest().body("Senha deve ter pelo menos 6 caracteres.");
        }
        // Verifica duplicação de e-mail
        if (profissionalSaudeRepository.findByEmail(profissional.getEmail()).isPresent()) {
            log.warn("Email já cadastrado: {}", profissional.getEmail());
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }
        // Define nome de usuário padrão, se não informado
        if (profissional.getNomeUsuario() == null || profissional.getNomeUsuario().isEmpty()) {
            log.info("Nome de usuário não informado, usando parte do email como padrão.");
            profissional.setNomeUsuario(profissional.getEmail().split("@")[0]);
        }
        
        if (profissional.getEspecialidade() == null || profissional.getEspecialidade().isEmpty()) {
            log.warn("Especialidade não informada para cadastro de profissional de saúde.");
            return ResponseEntity.badRequest().body("Especialidade é obrigatória.");
        }

        if (profissional.getRegistroProfissional() == null || profissional.getRegistroProfissional().isEmpty()) {
            log.warn("CRM não informado para cadastro de profissional de saúde.");
            return ResponseEntity.badRequest().body("CRM é obrigatório.");
            
        }

        // Verifica se o CRM já está cadastrado
        if (profissionalSaudeRepository.findByregistroProfissional(profissional.getRegistroProfissional()).isPresent()) {
            log.warn("CRM já cadastrado: {}", profissional.getRegistroProfissional());
            return ResponseEntity.badRequest().body("CRM já cadastrado.");
        }

        // Criptografa a senha
        profissional.setSenhaHash(passwordEncoder.encode(profissional.getSenhaHash()));
        ProfissionalSaude salvo = profissionalSaudeRepository.save(profissional);
        log.info("Profissional da saúde cadastrado com sucesso:{}", profissional.getNomeUsuario());
        return ResponseEntity.ok(salvo);
    }
    /**
     * Atualizar perfil do administrador.
     */
    @Operation(summary = "Atualizar Perfil do Administrador", description = "Atualiza o perfil de um administrador existente.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id,
            @Valid @RequestBody AtualizarPerfilAdministradorDTO dto) {
        var adminOpt = administradorRepository.findById(id);
        if (adminOpt.isEmpty()) {
            log.warn("Administrador não encontrado com ID: {}", id);
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
        log.info("Perfil do Administrador atualizado com sucesso: {}", admin.getNomeUsuario());
        return ResponseEntity.ok("Perfil do administrador atualizado com sucesso.");
    }

    /**
     * Listar todos os administradores.
     */
    @Operation(summary = "Listar Administradores", description = "Lista todos os administradores cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<?> listar() {
        log.info("Listando todos os administradores");
        return ResponseEntity.ok(administradorRepository.findAll());
    }

    /**
     * Endpoint para remover um paciente (exemplo de gerenciamento de cadastros).
     */
    @Operation(summary = "Remover Paciente", description = "Remove um paciente do sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remover-paciente/{id}")
    public ResponseEntity<?> removerPaciente(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
        log.info("Paciente removido com sucesso: ID{}", id);
        
        return ResponseEntity.ok("Paciente removido com sucesso.");
    }

    /**
     * Endpoint para remover um profissional de saúde.
     */
    @Operation(summary = "Remover Profissional de Saúde", description = "Remove um profissional de saúde do sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remover-profissional/{id}")
    public ResponseEntity<?> removerProfissional(@PathVariable Long id) {
        profissionalSaudeRepository.deleteById(id);
        log.info("Profissional de saúde removido com sucesso: ID{}", id);
        return ResponseEntity.ok("Profissional de saúde removido com sucesso.");
    }

    /**
     * Endpoint para gerar um relatório geral de cadastros.
     */
    @Operation(summary = "Gerar Relatório Geral", description = "Gera um relatório geral com o total de pacientes, profissionais de saúde e administradores.")
    @PreAuthorize("hasRole('ADMIN')") 
    @GetMapping("/gerar-relatorio")
    public ResponseEntity<?> gerarRelatorioGeral() {
        Map<String, Long> relatorio = new HashMap<>();
        relatorio.put("Total de Pacientes", pacienteRepository.count());
        relatorio.put("Total de Profissionais de Saúde", profissionalSaudeRepository.count());
        relatorio.put("Total de Administradores", (long) administradorRepository.findAll().size());
        log.info("Relatório geral gerado com sucesso.");
        return ResponseEntity.ok(relatorio);
    }

    /**
     * Endpoint para visualizar todas as internações.
     */
    @Operation(summary = "Listar Internações", description = "Lista todas as internações registradas no sistema.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFISSIONAL_SAUDE')")
    @GetMapping("/internacoes")
    public ResponseEntity<?> listarInternacoes() {
        log.info("Listando todas as internações");
        return ResponseEntity.ok(internacaoRepository.findAll());
    }

    /**
     * Endpoint para visualizar internações ativas (sem alta)
     */
    @Operation(summary = "Listar Internações Ativas", description = "Lista todas as internações ativas (sem alta) registradas no sistema.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFISSIONAL_SAUDE')")
    @GetMapping("/internacoes-ativas")
    public ResponseEntity<?> listarInternacoesAtivas() {
        List<Internacao> ativas = internacaoRepository.findAll()
                .stream()
                .filter(internacao -> internacao.getDataHoraSaida() == null)
                .collect(Collectors.toList());
        
            log.info("Listando internações ativas, total: {}", ativas.size());
            return ResponseEntity.ok(ativas);
    }
}
