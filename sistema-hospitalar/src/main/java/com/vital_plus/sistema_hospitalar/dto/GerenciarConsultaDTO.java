package com.vital_plus.sistema_hospitalar.dto;

import java.time.LocalDateTime;

import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GerenciarConsultaDTO {
    private Long consultaId; // usado para atualizar/cancelar
    private Long pacienteId;
    private Long profissionalId;
    private LocalDateTime dataHoraConsulta;
    private String motivoConsulta;
    private StatusConsulta status;
}
