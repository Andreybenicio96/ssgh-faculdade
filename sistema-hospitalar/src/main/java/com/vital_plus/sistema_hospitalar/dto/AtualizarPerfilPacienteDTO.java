package com.vital_plus.sistema_hospitalar.dto;

import com.vital_plus.sistema_hospitalar.model.Paciente;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter

public class AtualizarPerfilPacienteDTO {
    private String nomeUsuario;
    private String email;
    private String telefone;
    private String nomeCompleto;
    private String endereco;
    private String dataNascimento;
    private String sexo;
}
