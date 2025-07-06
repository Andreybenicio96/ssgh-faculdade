package com.vital_plus.sistema_hospitalar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "DTO para representar uma notificação de paciente.")
public class NotificacaoDTO {
    @NotNull(message = "O ID do paciente é obrigatório")
    @Schema(description = "ID do paciente que receberá a notificação.", example = "123")
    private Long pacienteId;

    @NotBlank(message = "A mensagem não pode ser vazia")
    @Schema(description = "Mensagem da notificação a ser enviada ao paciente.", example = "Sua consulta foi agendada para amanhã às 10:00.")
    private String mensagem;

    // Se vier nulo, no Controller usaremos LocalDateTime.now()
    @NotNull(message = "A data e hora são obrigatorias")
    @Schema(description = "Data e hora em que a notificação foi criada.", example = "2023-10-02T10:00:00")
    private LocalDateTime dataHora;
}
