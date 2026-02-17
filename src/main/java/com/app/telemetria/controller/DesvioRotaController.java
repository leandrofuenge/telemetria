package com.app.telemetria.controller;

import com.app.telemetria.entity.DesvioRota;
import com.app.telemetria.repository.DesvioRotaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/desvios")
public class DesvioRotaController {
    
    private final DesvioRotaRepository desvioRotaRepository;
    
    public DesvioRotaController(DesvioRotaRepository desvioRotaRepository) {
        this.desvioRotaRepository = desvioRotaRepository;
    }
    
    @GetMapping("/rota/{rotaId}")
    public List<DesvioRota> listarDesviosPorRota(@PathVariable Long rotaId) {
        return desvioRotaRepository.findByRotaIdOrderByDataHoraDesvioDesc(rotaId);
    }
    
    @GetMapping("/ativos")
    public List<DesvioRota> listarDesviosAtivos() {
        return desvioRotaRepository.findByResolvidoFalse();
    }
    
    @GetMapping("/veiculo/{veiculoId}")
    public List<DesvioRota> listarDesviosPorVeiculo(@PathVariable Long veiculoId) {
        return desvioRotaRepository.findByVeiculoIdOrderByDataHoraDesvioDesc(veiculoId);
    }
}