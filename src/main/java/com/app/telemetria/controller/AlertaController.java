package com.app.telemetria.controller;

import com.app.telemetria.entity.Alerta;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.repository.AlertaRepository;
import com.app.telemetria.service.VeiculoService;
import com.app.telemetria.exception.VeiculoNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    
    private final AlertaRepository alertaRepository;
    private final VeiculoService veiculoService;
    
    public AlertaController(AlertaRepository alertaRepository, VeiculoService veiculoService) {
        this.alertaRepository = alertaRepository;
        this.veiculoService = veiculoService;
    }
    
    @GetMapping
    public Page<Alerta> listarTodos(Pageable pageable) {
        return alertaRepository.findAll(pageable);
    }
    
    @GetMapping("/ativos")
    public List<Alerta> listarAtivos() {
        return alertaRepository.findByResolvidoFalseOrderByDataHoraDesc();
    }
    
    @GetMapping("/veiculo/{veiculoId}")
    public List<Alerta> listarPorVeiculo(@PathVariable Long veiculoId) {

        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        return alertaRepository.findByVeiculoOrderByDataHoraDesc(veiculo);
    }
    
    @GetMapping("/motorista/{motoristaId}")
    public List<Alerta> listarPorMotorista(@PathVariable Long motoristaId) {
        return alertaRepository.findByMotoristaIdOrderByDataHoraDesc(motoristaId);
    }
    
    @GetMapping("/viagem/{viagemId}")
    public List<Alerta> listarPorViagem(@PathVariable Long viagemId) {
        return alertaRepository.findByViagemIdOrderByDataHoraDesc(viagemId);
    }
    
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return Map.of(
            "totalAlertasNaoLidos", alertaRepository.countByResolvidoFalse(),
            "alertasCriticos", alertaRepository.countByGravidadeAndResolvidoFalse("CRITICA"),
            "alertasAltos", alertaRepository.countByGravidadeAndResolvidoFalse("ALTA"),
            "alertasMedios", alertaRepository.countByGravidadeAndResolvidoFalse("MEDIA"),
            "alertasBaixos", alertaRepository.countByGravidadeAndResolvidoFalse("BAIXA")
        );
    }
    
    @PutMapping("/{id}/ler")
    public Alerta marcarComoLido(@PathVariable Long id) {
        Alerta alerta = alertaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alerta não encontrado com id: " + id));
        alerta.setLido(true);
        alerta.setDataHoraLeitura(LocalDateTime.now());
        return alertaRepository.save(alerta);
    }
    
    @PutMapping("/{id}/resolver")
    public Alerta resolverAlerta(@PathVariable Long id) {
        Alerta alerta = alertaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alerta não encontrado com id: " + id));
        alerta.setResolvido(true);
        alerta.setDataHoraResolucao(LocalDateTime.now());
        return alertaRepository.save(alerta);
    }
    
    @GetMapping("/periodo")
    public List<Alerta> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return alertaRepository.findByPeriodo(inicio, fim);
    }
}