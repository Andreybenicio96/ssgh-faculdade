package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalSaude extends Usuario {
    @NotBlank(message = " O nome completo é obrigatorio.")
    private String nomeCompleto;

    @NotBlank(message = "A especialidade é obrigatoria.")
    private String especialidade;
    
    @NotBlank(message = "O registro profissional é obrigatorio.")
    private String registroProfissional;

    public void atualizarPerfilProfissional(String nomeUsuario, String email, String telefone,
            String especialidade, String crm) {
        atualizarPerfil(nomeUsuario, email, telefone);
        if (especialidade != null)
            this.especialidade = especialidade;
        if (registroProfissional != null)
            this.registroProfissional = crm;
    }
}
