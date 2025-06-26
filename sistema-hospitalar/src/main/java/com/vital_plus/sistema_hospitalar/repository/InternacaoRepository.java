package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.Internacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
@Repository
public interface InternacaoRepository extends JpaRepository<Internacao, Long> {
    Optional<Internacao> findById(Long id);
    
}
