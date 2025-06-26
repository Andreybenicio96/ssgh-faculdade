package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leitos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Leito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero; // número do leito

    private boolean ocupado; // está ocupado ou não

    private LocalDateTime dataHoraOcupacao; // quando foi ocupado

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente; // paciente no leito
}
