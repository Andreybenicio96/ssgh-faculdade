package com.vital_plus.sistema_hospitalar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProntuarioDTO {

    @NotNull
    private Long pacienteId;

    @NotBlank
    private String descricao;

    private String observacoes;
}
