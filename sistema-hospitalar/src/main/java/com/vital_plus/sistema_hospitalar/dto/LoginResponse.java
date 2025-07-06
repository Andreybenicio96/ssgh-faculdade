package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para resposta de login.")
public class LoginResponse {
    @NotBlank(message = "O token é obrigatorio.")
    @Schema(description = "ID do usuário autenticado.", example = "1")
    private String token;

    @NotBlank(message = "O nome de usuario é obrigatorio.")
    @Schema(description = "Nome de usuário do usuário autenticado.", example = "usuario123")
    private String role;
}
