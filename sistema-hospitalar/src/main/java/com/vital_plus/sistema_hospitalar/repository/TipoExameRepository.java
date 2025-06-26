package com.vital_plus.sistema_hospitalar.repository;

import org.springframework.stereotype.Repository;
import com.vital_plus.sistema_hospitalar.model.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface TipoExameRepository extends JpaRepository<TipoExame, Long> {

   

}
