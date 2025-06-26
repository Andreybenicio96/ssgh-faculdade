package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Usuario {
    private String nomeCompleto;
    private String cargo;
    private String departamento;

    public void atualizarPerfilAdministrador(String nomeUsuario, String email, String telefone,
            String departamento) {
        atualizarPerfil(nomeUsuario, email, telefone);
        if (departamento != null)
            this.departamento = departamento;
    }
}
