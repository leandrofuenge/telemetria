package com.app.telemetria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.telemetria.dto.VeiculoDTO;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.service.VeiculoService;

@RestController
@RequestMapping("/api/v1/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService service;

    @PostMapping
    public VeiculoDTO criar(@RequestBody Veiculo veiculo) {
        return service.salvar(veiculo);
    }

    @GetMapping
    public List<VeiculoDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public VeiculoDTO buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public VeiculoDTO atualizar(@PathVariable Long id,
                                @RequestBody Veiculo veiculo) {
        return service.atualizar(id, veiculo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
