package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para requisição de login.")
public class LoginRequest {
    @Email(message = "O email deve ser válido.")
    @Schema(description = "Email do usuário para login.", example = "paciente@gmail.com")
    private String email;

    @NotBlank(message = "A senha é obrigatoria.")
    @Schema(description = "Senha do usuário para login.", example = "senha123")
    private String senha;
}
