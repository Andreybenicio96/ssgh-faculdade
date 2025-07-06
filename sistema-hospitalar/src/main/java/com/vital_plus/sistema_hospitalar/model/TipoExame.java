package com.vital_plus.sistema_hospitalar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Classe que representa o tipo de exame.
 * 
 * @author Vital Plus
 * @version 1.0
 */
@Entity
@Table(name = "tipo_exame")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TipoExame {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O nome do tipo de exame é obrigatório.")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "A descrição do tipo de exame é obrigatória.")
    private String descricao;

    @Column(nullable = false)
    private boolean ativo;

    public TipoExame(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true; // Por padrão, o tipo de exame é ativo
    }
}
