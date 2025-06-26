package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
/**
 * Classe que representa uma Notificação no sistema hospitalar.
 * As notificações podem ser usadas para alertar os usuários sobre eventos importantes,
 * como consultas agendadas, resultados de exames, entre outros.
 */
@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID da notificação

    @ManyToOne
    private Paciente paciente; // Usuário que receberá a notificação

    private String mensagem; // Mensagem da notificação

    private boolean lida; // Indica se a notificação foi lida ou não

    private LocalDateTime dataHora; // Data e hora em que a notificação foi criada
}
