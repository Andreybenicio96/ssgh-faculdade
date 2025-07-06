package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teleconsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @Column(nullable = false)
    @NotBlank(message = "O link de acesso é obrigatório.")
    private String linkAcesso;

    @Column(nullable = false)
    @NotNull(message = "A data e hora da teleconsulta é obrigatória.")
    private LocalDateTime dataHoraCriacao;
}
