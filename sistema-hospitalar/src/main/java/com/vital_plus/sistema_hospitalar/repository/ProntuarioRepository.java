package com.vital_plus.sistema_hospitalar.repository;

import org.springframework.stereotype.Repository;
import com.vital_plus.sistema_hospitalar.model.Prontuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    // Método para buscar prontuários por ID do paciente
    List<Prontuario> findByPacienteId(Long pacienteId);
}
