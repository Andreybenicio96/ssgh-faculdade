package com.vital_plus.sistema_hospitalar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificacaoDTO {
    @NotNull(message = "O ID do paciente é obrigatório")
    private Long pacienteId;

    @NotBlank(message = "A mensagem não pode ser vazia")
    private String mensagem;

    // Se vier nulo, no Controller usaremos LocalDateTime.now()
    private LocalDateTime dataHora;
}
