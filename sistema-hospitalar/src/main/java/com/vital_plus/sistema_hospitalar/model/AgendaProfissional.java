package com.vital_plus.sistema_hospitalar.model;

import com.vital_plus.sistema_hospitalar.enums.TipoConsulta;

import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "agenda_profissional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaProfissional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O tipo de consulta é obrigatório.")
    private LocalDateTime dataHora; // Data e hora da consulta

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissionalSaude; // Profissional de saúde responsável pela agenda
}
