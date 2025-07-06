package com.vital_plus.sistema_hospitalar.model;



import java.time.LocalDateTime;

import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Classe que representa uma consulta médica no sistema hospitalar.
 * Esta classe pode ser expandida para incluir detalhes específicos da consulta,
 * como data, hora, médico responsável, paciente, etc.
 */
@Entity
@Table(name = "consultas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataHora; // Data e hora da consulta
    
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    // Relacionamento com a entidade Paciente
    private Paciente paciente; // Nome do paciente
    
    @NotBlank(message = "O motivo da consulta é obrigatorio.")
    private String motivoConsulta; // Motivo da consulta

    
    private String observacoes; // Observações adicionais sobre a consulta
    
    
    @Enumerated(EnumType.STRING)
    private StatusConsulta status; // Status da consulta (agendada, realizada, cancelada, etc.)

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissionalSaude; // Profissional de saúde responsável pela consulta

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
