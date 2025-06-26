package com.vital_plus.sistema_hospitalar.repository;


import org.springframework.stereotype.Repository;
import com.vital_plus.sistema_hospitalar.model.Exame;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
 
}
