package com.vital_plus.sistema_hospitalar.repository;


import org.springframework.stereotype.Repository;
import com.vital_plus.sistema_hospitalar.model.AgendaProfissional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface AgendaProfissionalRepository extends JpaRepository<AgendaProfissional, Long> {
    List<AgendaProfissional> findByProfissionalSaudeId(Long profissionalId);
}
