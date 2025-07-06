package com.vital_plus.sistema_hospitalar.model;

import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "O nome completo é obrigatorio.")
    private String nomeCompleto;

    @NotBlank(message = "O endereço é obrigatorio.")
    private String endereco;

    @NotNull(message = "A data de nascimento é obrigatoria.")
    private LocalDate dataNascimento;

    @NotBlank(message = "O sexo é obrigatorio.")
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

