package com.app.telemetria.controller;

import com.app.telemetria.entity.Telemetria;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.repository.TelemetriaRepository;
import com.app.telemetria.repository.VeiculoRepository;
import com.app.telemetria.service.AlertaService;
import com.app.telemetria.exception.VeiculoNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/telemetria")
public class TelemetriaController {
    
    private final TelemetriaRepository telemetriaRepository;
    private final VeiculoRepository veiculoRepository;
    private final AlertaService alertaService;  
    
    public TelemetriaController(
            TelemetriaRepository telemetriaRepository,
            VeiculoRepository veiculoRepository,
            AlertaService alertaService) {  
        this.telemetriaRepository = telemetriaRepository;
        this.veiculoRepository = veiculoRepository;
        this.alertaService = alertaService;
    }
    
    @PostMapping
    public ResponseEntity<Telemetria> criar(@RequestBody TelemetriaRequest request) {
        // Buscar o veículo
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculo().getId())
            .orElseThrow(() -> new VeiculoNotFoundException(request.getVeiculo().getId()));
        
        // Criar nova telemetria
        Telemetria telemetria = new Telemetria();
        telemetria.setVeiculo(veiculo);
        telemetria.setLatitude(request.getLatitude());
        telemetria.setLongitude(request.getLongitude());
        telemetria.setVelocidade(request.getVelocidade());
        telemetria.setNivelCombustivel(request.getNivelCombustivel()); 
        telemetria.setDataHora(request.getDataHora() != null ? 
            request.getDataHora() : LocalDateTime.now());
        
        // Salvar telemetria
        Telemetria saved = telemetriaRepository.save(telemetria);
        
        // ===== GERAR ALERTAS BASEADO NA TELEMETRIA =====
        alertaService.processarTelemetria(saved);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @GetMapping("/veiculo/{veiculoId}")
    public List<Telemetria> listarPorVeiculo(@PathVariable Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new VeiculoNotFoundException(veiculoId));
        return telemetriaRepository.findByVeiculoOrderByDataHoraDesc(veiculo);
    }
    
    @GetMapping("/veiculo/{veiculoId}/ultima")
    public ResponseEntity<Telemetria> ultimaTelemetria(@PathVariable Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new VeiculoNotFoundException(veiculoId));
        
        return telemetriaRepository.findUltimaTelemetriaByVeiculo(veiculo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/veiculo/{veiculoId}/periodo")
    public List<Telemetria> listarPorPeriodo(
            @PathVariable Long veiculoId,
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
            .orElseThrow(() -> new VeiculoNotFoundException(veiculoId));
        
        return telemetriaRepository.findByVeiculoAndDataHoraBetweenOrderByDataHoraAsc(
            veiculo, inicio, fim);
    }
    
    // DTO para receber os dados
    public static class TelemetriaRequest {
        private VeiculoRequest veiculo;
        private Double latitude;
        private Double longitude;
        private Double velocidade;
        private Double nivelCombustivel;  
        private LocalDateTime dataHora;
        
        // Getters e Setters
        public VeiculoRequest getVeiculo() { return veiculo; }
        public void setVeiculo(VeiculoRequest veiculo) { this.veiculo = veiculo; }
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        
        public Double getVelocidade() { return velocidade; }
        public void setVelocidade(Double velocidade) { this.velocidade = velocidade; }
        
        public Double getNivelCombustivel() { return nivelCombustivel; }  // NOVO
        public void setNivelCombustivel(Double nivelCombustivel) { this.nivelCombustivel = nivelCombustivel; }
        
        public LocalDateTime getDataHora() { return dataHora; }
        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    }
    
    public static class VeiculoRequest {
        private Long id;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
}