package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para atualizar o perfil de um profissional de saúde.")
public class AtualizarPerfilProfissionalDTO {
    @NotBlank(message = "O nome de usuário é obrigatorio.")
    @Schema(description = "Nome de usuário do profissional de saúde.", example = "profissional123")
    private String nomeUsuario;

    @NotBlank(message = "O email é obrigatorio.")
    @Schema(description = "Email do profissional de saúde.", example = "profissional@gmail.com")
    private String email;

    @NotBlank(message = "O telefone é obrigatorio.")
    @Schema(description = "Telefone do profissional de saúde.", example = "(11) 99876-5432")
    private String telefone;

    @NotBlank(message = "A especialidade é obrigatoria.")
    @Schema(description = "Especialidade do profissional de saúde.", example = "Cardiologia")
    private String especialidade;

    @NotBlank(message = "O registro profissional é obrigatorio.")
    @Schema(description = "Registro profissional do profissional de saúde (CRM, COREN, etc.).", example = "CRM 123456")
    private String registroProfissional; // CRM, COREN, etc.
}
