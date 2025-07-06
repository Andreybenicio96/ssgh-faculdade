package com.vital_plus.sistema_hospitalar.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para representar uma receita médica.")
public class ReceitaMedicaDTO {
    @NotNull(message = "O ID do paciente é obrigatorio.")
    @Schema(description = "ID do paciente ao qual a receita pertence.", example = "1")
    private Long pacienteId;

    @NotNull(message = "O ID do profissional de saúde é obrigatorio.")
    @Schema(description = "ID do profissional de saúde que emitiu a receita.", example = "456")
    private Long profissionalId;

    @NotBlank(message = "O conteudo da receita é obrigatorio.")
    @Schema(description = "Conteúdo da receita médica, incluindo medicamentos e dosagens.", example = "Paracetamol 500mg, tomar 1 comprimido a cada 8 horas por 5 dias.")
    private String conteudo;

    @NotNull(message = "A data de emissão é obrigatoria.")
    @Schema(description = "Data de emissão da receita médica.", example = "2023-10-02")
    private LocalDate dataEmissao; // Usar String para facilitar a conversão no Controller,
}
