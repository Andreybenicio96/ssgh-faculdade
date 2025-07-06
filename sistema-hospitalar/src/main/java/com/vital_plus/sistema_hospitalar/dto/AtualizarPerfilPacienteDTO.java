package com.vital_plus.sistema_hospitalar.dto;

import com.vital_plus.sistema_hospitalar.model.Paciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "DTO para atualizar o perfil de um paciente.")
public class AtualizarPerfilPacienteDTO {
    @NotBlank(message = "O nome de usuário é obrigatorio.")
    @Schema(description = "Nome de usuário do paciente.", example = "paciente123")
    private String nomeUsuario;
    
    @Email(message = "O email deve ser válido.")
    @Schema(description = "Email do paciente.", example = "paciente@gmail.com")
    private String email;
    
    @NotBlank(message = "O telefone é obrigatorio.")
    @Schema(description = "Telefone do paciente.", example = "(11) 98765-4321")
    private String telefone;
    
    @NotBlank(message = "O nome completo é obrigatorio.")
    @Schema(description = "Nome completo do paciente.", example = "João da silva")
    private String nomeCompleto;

    @NotBlank(message = "O endereço é obrigatorio.")
    @Schema(description = "Endereço do paciente.", example = "Rua das Flores, 123, São Paulo, SP")
    private String endereco;
    
    @NotNull(message = "A data de nascimento é obrigatoria.")
    @Schema(description = "Data de nascimento do paciente.", example = "1990-01-01")
    private String dataNascimento;
    
    @NotBlank(message = "O sexo é obrigatorio.")
    @Schema(description = "Sexo do paciente.", example = "Masculino")
    private String sexo;
}
