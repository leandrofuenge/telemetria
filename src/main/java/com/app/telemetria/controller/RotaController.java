package com.app.telemetria.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.app.telemetria.entity.Rota;
import com.app.telemetria.service.RotaService;

@RestController
@RequestMapping("/rotas")
public class RotaController {
    
    private final RotaService service;
    
    public RotaController(RotaService service) {
        this.service = service;
    }
    
    @PostMapping
    public Rota criar(@RequestBody Rota rota) {
        return service.salvar(rota);
    }
    
    @GetMapping
    public List<Rota> listar() {
        return service.listar();
    }
    
    @GetMapping("/{id}")
    public Rota buscar(@PathVariable Long id) {
        return service.buscarPorId(id);  
    }
    
    @PutMapping("/{id}")  
    public Rota atualizar(@PathVariable Long id, @RequestBody Rota rota) {
        return service.atualizar(id, rota);
    }
    
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}