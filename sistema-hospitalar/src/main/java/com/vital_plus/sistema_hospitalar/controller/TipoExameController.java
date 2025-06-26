package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import com.vital_plus.sistema_hospitalar.model.TipoExame;
import com.vital_plus.sistema_hospitalar.repository.TipoExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/tipos-exame")
public class TipoExameController {
    @Autowired
    private TipoExameRepository tipoExameRepository;

    /**
     * Endpoint para cadastrar um tipo de exame.
     * @param tipoExame Dados do tipo de exame a ser cadastrado
     * @return Tipo de exame salvo
     */
    @PostMapping
    public ResponseEntity<TipoExame> salvar(@RequestBody TipoExame tipoExame) {
        TipoExame salvo = tipoExameRepository.save(tipoExame);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Endpoint para listar todos os tipos de exame.
     * @return Lista de tipos de exame
     */
    @GetMapping
    public ResponseEntity<List<TipoExame>> listar() {
        List<TipoExame> tipos = tipoExameRepository.findAll();
        return ResponseEntity.ok(tipos);
    }
}
