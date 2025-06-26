package com.vital_plus.sistema_hospitalar.controller;

import com.vital_plus.sistema_hospitalar.dto.IniciarTeleconsultaDTO;
import com.vital_plus.sistema_hospitalar.model.Consulta;
import com.vital_plus.sistema_hospitalar.model.Teleconsulta;
import com.vital_plus.sistema_hospitalar.repository.ConsultaRepository;
import com.vital_plus.sistema_hospitalar.repository.TeleconsultaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/teleconsulta")
public class TeleconsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TeleconsultaRepository teleconsultaRepository;

    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciar(@RequestBody IniciarTeleconsultaDTO dto) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(dto.getConsultaId());

        if (consultaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Consulta não encontrada.");
        }

        Consulta consulta = consultaOpt.get();

        if (teleconsultaRepository.findByConsultaId(consulta.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("Teleconsulta já iniciada para esta consulta.");
        }

        Teleconsulta teleconsulta = Teleconsulta.builder()
                .consulta(consulta)
                .linkAcesso("https://meet.jit.si/vitalplus-consulta-" + consulta.getId()) // simulando link
                .dataHoraCriacao(LocalDateTime.now())
                .build();

        teleconsultaRepository.save(teleconsulta);
        return ResponseEntity.ok(teleconsulta);
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<?> buscarPorConsulta(@PathVariable Long consultaId) {
        return teleconsultaRepository.findByConsultaId(consultaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
