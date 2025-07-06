package com.vital_plus.sistema_hospitalar.model;

import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "O nome de usuário é obrigatório.")
    private String nomeUsuario;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "A senha é obrigatória.")
    private String senhaHash; // Senha criptografada

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    //private boolean ativo;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    // Opcional: método de autenticação básico
    public boolean autenticar(String senhaPlana, String senhaCriptografada) {
        return senhaHash.equals(senhaCriptografada); // substituído na prática por BCrypt.matches()
    }
    // Opcional: método para verificar se o usuário é do tipo administrador
    public void atualizarPerfil(String nomeUsuario, String email, String telefone) {
        if (nomeUsuario != null) this.nomeUsuario = nomeUsuario;
        if (email != null) this.email = email;
        if (telefone != null) this.telefone = telefone;
    }
}
