package com.vital_plus.sistema_hospitalar.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "internacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Internacao {
    @Id
    @GeneratedValue
    private Long id; // ID da internação
    
    @ManyToOne
    private Paciente pacienteId; // ID do paciente internado
    
    @ManyToOne
    private Leito leito;// Leito onde o paciente está internado
    
    @NotBlank(message = "O quarto é obrigatorio.")
    private String quarto; // Quarto onde o paciente está internado
     
    @NotBlank(message = "O motivo da internação é obrigatorio.")
    private String motivoInternacao; // Motivo da internação

    @NotNull(message = "A data de internação é obrigatoria.")
    private LocalDateTime dataHoraEntrada; // Data e hora de entrada na internação
    
    @Column(nullable = true)
    private LocalDateTime dataHoraSaida; // Data e hora de saída da internação (pode ser nulo se ainda estiver internado)
}
