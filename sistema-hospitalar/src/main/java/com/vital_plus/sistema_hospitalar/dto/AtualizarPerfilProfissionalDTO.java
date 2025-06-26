package com.vital_plus.sistema_hospitalar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarPerfilProfissionalDTO {
    private String nomeUsuario;
    private String email;
    private String telefone;
    private String especialidade;
    private String registroProfissional; // CRM, COREN, etc.
}
