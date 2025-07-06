package com.vital_plus.sistema_hospitalar.dto;

import java.time.LocalDateTime;

import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(description = "DTO para gerenciar consultas médicas.")
public class GerenciarConsultaDTO {
    
    @Schema(description = "ID da consulta médica.", example = "1")
    private Long consultaId;
    
    @NotNull(message = "O ID do paciente é obrigatorio.")
    @Schema(description = "ID do paciente associado à consulta.", example = "123")
    private Long pacienteId;

    @NotNull(message = "O ID do profissional de saúde é obrigatorio.")
    @Schema(description = "ID do profissional de saúde que realizará a consulta.", example = "456")
    private Long profissionalId;

    @NotNull(message = "A data e hora da consulta são obrigatorias.")
    @Schema(description = "Data e hora agendada para a consulta.", example = "2023-10-02T10:00:00")
    private LocalDateTime dataHoraConsulta;

    @NotBlank(message = "O motivo da consulta é obrigatorio.")
    @Schema(description = "Motivo da consulta médica.", example = "Dor de cabeça persistente")
    private String motivoConsulta;

    //@NotBlank(message = "O status da consulta é obrigatorio.")
    @Schema(description = "Status da consulta médica (agendada, realizada, cancelada, etc.).", example = "AGENDADA")
    private StatusConsulta status;
}
