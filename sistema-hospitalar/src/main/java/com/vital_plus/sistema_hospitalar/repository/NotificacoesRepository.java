package com.vital_plus.sistema_hospitalar.repository;

import com.vital_plus.sistema_hospitalar.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacoesRepository extends JpaRepository<Notificacao, Long> {

    @Query("SELECT n FROM Notificacao n WHERE n.paciente.id = :pacienteId")
    List<Notificacao> findByPacienteId(@Param("pacienteId") Long pacienteId);

}
