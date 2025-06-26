package com.vital_plus.sistema_hospitalar.dto;

import lombok.Data;

@Data
public class ReceitaMedicaDTO {
    private Long pacienteId;
    private Long profissionalId;
    private String conteudo;
}
