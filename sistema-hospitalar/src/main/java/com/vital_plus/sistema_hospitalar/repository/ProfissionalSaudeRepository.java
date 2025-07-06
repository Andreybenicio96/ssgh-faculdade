package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.Administrador;
import com.vital_plus.sistema_hospitalar.model.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
    Optional<ProfissionalSaude> findByEmail(String email);

    Optional<ProfissionalSaude> findByregistroProfissional(String registroProfissional);

    
}
