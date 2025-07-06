package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para representar um prontuário médico.")
public class ProntuarioDTO {

    @NotNull(message = "O ID do paciente é obrigatorio.")
    @Schema(description = "ID do paciente ao qual o prontuário pertence.", example = "1")
    private Long pacienteId;

    @NotBlank(message = "A descrição do prontuário é obrigatoria.")
    @Schema(description = "Descrição do prontuário médico.", example = "Paciente apresenta sintomas de febre e tosse.")
    private String descricao;

    @NotBlank(message = "As observações são obrigatorias.")
    @Schema(description = "Observações adicionais do prontuário médico.", example = "Paciente deve retornar em 7 dias para reavaliação.")
    private String observacoes;
}
