package com.vital_plus.sistema_hospitalar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para iniciar uma teleconsulta.")
public class IniciarTeleconsultaDTO {
    
    @NotNull(message = "O ID da consulta é obrigatorio")
    @Schema(description = "ID da consulta médica para iniciar a teleconsulta.", example = "1")
    private Long consultaId;
}
