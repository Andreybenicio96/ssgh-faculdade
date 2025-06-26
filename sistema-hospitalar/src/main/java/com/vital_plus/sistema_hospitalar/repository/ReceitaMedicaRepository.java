package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.ReceitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceitaMedicaRepository extends JpaRepository<ReceitaMedica, Long> {
    List<ReceitaMedica> findByPacienteId(Long pacienteId);

    List<ReceitaMedica> findByProfissionalSaudeId(Long profissionalId);
}
