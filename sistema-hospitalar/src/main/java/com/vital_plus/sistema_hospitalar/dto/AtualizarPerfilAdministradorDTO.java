package com.vital_plus.sistema_hospitalar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarPerfilAdministradorDTO {
    private String nomeUsuario;
    private String email;
    private String telefone;
    private String departamento;
}
