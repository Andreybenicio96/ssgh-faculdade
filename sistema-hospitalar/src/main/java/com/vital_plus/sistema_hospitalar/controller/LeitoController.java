package com.vital_plus.sistema_hospitalar.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import com.vital_plus.sistema_hospitalar.model.Leito;
import com.vital_plus.sistema_hospitalar.repository.LeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/leitos")
public class LeitoController {
    @Autowired
    private LeitoRepository leitoRepository;

    /**
     * Endpoint para cadastrar um leito.
     * @param leito Dados do leito a ser cadastrado
     * @return Leito salvo
     */
    @PostMapping
    public ResponseEntity<Leito> salvar(@RequestBody Leito leito) {
        Leito salvo = leitoRepository.save(leito);
        return ResponseEntity.ok(salvo);
    }

    /**
     * Endpoint para listar todos os leitos.
     * @return Lista de leitos
     */
    @GetMapping
    public ResponseEntity<List<Leito>> listar() {
        List<Leito> leitos = leitoRepository.findAll();
        return ResponseEntity.ok(leitos);
    }
}
