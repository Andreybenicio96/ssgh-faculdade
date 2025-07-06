package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Usuario {
    @NotBlank(message = "O nome completo é obrigatorio.")
    private String nomeCompleto;

    @NotBlank(message = "O cargo é obrigatorio.")
    private String cargo;

    @NotBlank(message = "O departamento é obrigatorio.")
    private String departamento;

    public void atualizarPerfilAdministrador(String nomeUsuario, String email, String telefone,
            String departamento) {
        atualizarPerfil(nomeUsuario, email, telefone);
        if (departamento != null)
            this.departamento = departamento;
    }
}
