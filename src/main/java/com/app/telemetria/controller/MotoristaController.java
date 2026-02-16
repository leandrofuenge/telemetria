package com.app.telemetria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.telemetria.dto.MotoristaDTO;
import com.app.telemetria.entity.Motorista;
import com.app.telemetria.service.MotoristaService;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    @Autowired
    private MotoristaService service;

    @PostMapping
    public Motorista criar(@RequestBody MotoristaDTO dto) {
        Motorista motorista = new Motorista();
        motorista.setNome(dto.getNome());
        motorista.setCpf(dto.getCpf());
        motorista.setCnh(dto.getCnh());
        motorista.setCategoriaCnh(dto.getCategoriaCnh());
        
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
                                @RequestBody MotoristaDTO dto) {
        Motorista motorista = service.buscarPorId(id);
        
        motorista.setNome(dto.getNome());
        motorista.setCpf(dto.getCpf());
        motorista.setCnh(dto.getCnh());
        motorista.setCategoriaCnh(dto.getCategoriaCnh());
        
        return service.salvar(motorista);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}