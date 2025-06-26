package com.vital_plus.sistema_hospitalar.repository;

import org.springframework.stereotype.Repository;
import com.vital_plus.sistema_hospitalar.model.Leito;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface LeitoRepository extends JpaRepository<Leito, Long> {

   
}
