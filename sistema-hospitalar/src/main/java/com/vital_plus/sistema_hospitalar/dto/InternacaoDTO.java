package com.vital_plus.sistema_hospitalar.dto;

import lombok.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para representar uma internação de paciente.")
public class InternacaoDTO {

    @NotNull(message = "O ID do paciente é obrigatório.")
    @Schema(description = "ID do paciente que está sendo internado.", example = "123")
    private Long pacienteId;

    @NotNull(message = "O ID do leito é obrigatório.")
    @Schema(description = "ID do leito onde o paciente será internado.", example = "456")
    private Long leitoId;

    @NotBlank(message = "O quarto é obrigatório.")
    @Schema(description = "Quarto onde o paciente será internado.", example = "A-101")
    private String quarto;

    @NotBlank(message = "O motivo da internação é obrigatório.")
    @Schema(description = "Motivo da internação do paciente.", example = "Tratamento de pneumonia")
    private String motivoInternacao;

    @NotNull(message = "A data e hora de entrada são obrigatórias.")
    @Schema(description = "Data e hora de entrada do paciente na internação.", example = "2025-07-05T10:00:00")
    private LocalDateTime dataHoraEntrada;

    @Schema(description = "Data e hora de saída do paciente da internação, se aplicável.", example = "2025-07-10T15:00:00")
    private LocalDateTime dataHoraSaida;
}
