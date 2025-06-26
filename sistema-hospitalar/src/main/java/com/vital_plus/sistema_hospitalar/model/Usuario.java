package com.vital_plus.sistema_hospitalar.model;

import com.vital_plus.sistema_hospitalar.enums.TipoUsuario;
import jakarta.persistence.*;
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
    private String nomeUsuario;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senhaHash; // Senha criptografada

    private String telefone;

    private boolean ativo;

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
