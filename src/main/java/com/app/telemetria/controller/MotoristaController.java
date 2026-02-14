package com.app.telemetria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.telemetria.entity.Motorista;
import com.app.telemetria.service.MotoristaService;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    @Autowired
    private MotoristaService service;

    @PostMapping
    public Motorista criar(@RequestBody Motorista motorista) {
        return service.salvar(motorista);
    }

    @GetMapping
    public List<Motorista> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Motorista buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Motorista atualizar(@PathVariable Long id,
                                @RequestBody Motorista motorista) {
        return service.atualizar(id, motorista);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
