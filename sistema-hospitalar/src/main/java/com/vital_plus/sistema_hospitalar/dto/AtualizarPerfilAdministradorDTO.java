package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para atualizar o perfil de um administrador.")
/**
 * DTO para atualizar o perfil de um administrador.
 * Contém os campos necessários para atualizar as informações do administrador.
 */
public class AtualizarPerfilAdministradorDTO {
    @NotBlank(message = "O nome de usuário é obrigatorio.")
    @Schema(description = "Nome de usuário do administrador.", example = "admin123")
    private String nomeUsuario;
    
    @NotBlank(message = "O email é obrigatorio.")
    @Schema(description = "Email do administrador.", example = "administrador@gmail.com")
    private String email;
    
    @NotBlank(message = "O telefone é obrigatorio.")
    @Schema(description = "Telefone do administrador.", example = "(11) 99465-4321")
    private String telefone;
    
    @NotBlank(message = "Odepartamento é obrigatorio")
    @Schema(description = "Departamento do administrador.", example = "Recursos Humanos")
    private String departamento;
}
