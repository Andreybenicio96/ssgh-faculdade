package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);

    
}
