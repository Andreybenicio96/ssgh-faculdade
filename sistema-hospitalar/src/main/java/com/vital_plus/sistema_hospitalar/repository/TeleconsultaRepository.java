package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.Teleconsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeleconsultaRepository extends JpaRepository<Teleconsulta, Long> {
    Optional<Teleconsulta> findByConsultaId(Long consultaId);
}
