package com.vital_plus.sistema_hospitalar.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternacaoDTO {
    private Long pacienteId;
    private Long leitoId;
    private String motivoInternacao;
    private LocalDateTime dataHoraEntrada; // Pode ser opcional no POST
}
