package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalSaude extends Usuario {
    private String nomeCompleto;
    private String especialidade;
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
