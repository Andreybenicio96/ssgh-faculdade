package com.vital_plus.sistema_hospitalar.model;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Classe que representa um paciente no sistema hospitalar.
 * Herda de Usuario e adiciona informações específicas do paciente.
 */
@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario {
    
    private String nomeCompleto;
    private String endereco;
    private LocalDate dataNascimento;
    private String sexo;

    public void atualizarPerfilPaciente(String nomeUsuario, String email, String telefone,
                                        String nomeCompleto, String endereco, LocalDate dataNascimento, String sexo) {
        // Chama atualização da superclasse
        atualizarPerfil(nomeUsuario, email, telefone);

        // Atualiza campos específicos
        if (nomeCompleto != null) this.nomeCompleto = nomeCompleto;
        if (endereco != null) this.endereco = endereco;
        if (dataNascimento != null) this.dataNascimento = dataNascimento;
        if (sexo != null) this.sexo = sexo;
}
}

