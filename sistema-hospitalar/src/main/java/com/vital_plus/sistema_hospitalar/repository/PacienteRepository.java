package com.vital_plus.sistema_hospitalar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vital_plus.sistema_hospitalar.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByEmail(String email);
    
}
