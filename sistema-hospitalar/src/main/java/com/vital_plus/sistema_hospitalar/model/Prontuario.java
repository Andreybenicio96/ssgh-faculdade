package com.vital_plus.sistema_hospitalar.model;

import com.vital_plus.sistema_hospitalar.enums.TipoConsulta;

import com.vital_plus.sistema_hospitalar.enums.StatusConsulta;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "prontuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prontuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao; // Descrição do prontuário
    private String observacoes; // Observações adicionais do prontuário

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente; // Paciente associado ao prontuário
}
