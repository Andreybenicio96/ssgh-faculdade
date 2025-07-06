package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * Classe que representa um exame no sistema hospitalar.
 * Utiliza Lombok para gerar automaticamente os métodos getters, setters, equals, hashCode e toString.
 */
@Entity
@Table(name = "exames")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data do exame é obrigatoria.")
    private LocalDate dataExame; // Data em que o exame foi realizado
    
    @NotBlank(message = "O nome do exame é obrigatorio.")
    private String resultado; // Resultado do exame

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente; // Paciente a quem o exame pertence

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissionalSaude; // Profissional de saúde que realizou o exame

    @ManyToOne
    @JoinColumn(name= "tipo_exame_id")
    private TipoExame tipoExame; // Tipo de exame realizado, referenciando a entidade TipoExame
}
