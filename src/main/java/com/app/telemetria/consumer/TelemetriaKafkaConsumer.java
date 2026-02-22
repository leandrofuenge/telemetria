package com.app.telemetria.consumer;

import com.app.telemetria.entity.Telemetria;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.repository.TelemetriaRepository;
import com.app.telemetria.repository.VeiculoRepository;
import com.app.telemetria.repository.ViagemRepository;
import com.app.telemetria.service.AlertaService;
import com.app.telemetria.service.WeatherAlertService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TelemetriaKafkaConsumer {

    private final TelemetriaRepository telemetriaRepository;
    private final VeiculoRepository veiculoRepository;
    private final ViagemRepository viagemRepository;
    private final AlertaService alertaService;
    private final WeatherAlertService weatherAlertService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelemetriaKafkaConsumer(
            TelemetriaRepository telemetriaRepository,
            VeiculoRepository veiculoRepository,
            ViagemRepository viagemRepository,
            AlertaService alertaService,
            WeatherAlertService weatherAlertService) {
        this.telemetriaRepository = telemetriaRepository;
        this.veiculoRepository = veiculoRepository;
        this.viagemRepository = viagemRepository;
        this.alertaService = alertaService;
        this.weatherAlertService = weatherAlertService;
    }

    @KafkaListener(topics = "telemetria-raw", groupId = "telemetria-group", concurrency = "3")
    public void processarTelemetria(String mensagem) {
        try {
            // Converter JSON para objeto
            JsonNode json = objectMapper.readTree(mensagem);
            
            Long veiculoId = json.get("vehicle_id").asLong();
            
            // Buscar veículo no banco
            Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + veiculoId));
            
            // Criar entidade Telemetria
            Telemetria telemetria = new Telemetria();
            telemetria.setVeiculo(veiculo);
            telemetria.setLatitude(json.get("latitude").asDouble());
            telemetria.setLongitude(json.get("longitude").asDouble());
            telemetria.setVelocidade(json.get("velocidade").asDouble());
            
            if (json.has("nivelCombustivel")) {
                telemetria.setNivelCombustivel(json.get("nivelCombustivel").asDouble());
            }
            
            // Timestamp do veículo ou atual
            if (json.has("timestamp")) {
                long ts = json.get("timestamp").asLong();
                telemetria.setDataHora(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(ts), ZoneId.systemDefault()));
            } else {
                telemetria.setDataHora(LocalDateTime.now());
            }
            
            // 1. Salvar no banco
            Telemetria saved = telemetriaRepository.save(telemetria);
            
            // 2. Buscar viagem ativa
            var viagemAtiva = viagemRepository.findByVeiculoAndStatus(veiculo, "EM_ANDAMENTO")
                .orElse(null);
            
            // 3. Gerar alertas de telemetria (velocidade, combustível, etc)
            alertaService.processarTelemetria(saved);
            
            // 4. Gerar alertas climáticos (se houver coordenadas válidas)
            if (telemetria.getLatitude() != null && telemetria.getLongitude() != null) {
                weatherAlertService.verificarClimaParaVeiculo(
                    veiculo.getId(),
                    telemetria.getLatitude(),
                    telemetria.getLongitude(),
                    viagemAtiva
                );
            }
            
            System.out.println("✅ Telemetria processada: Veículo " + veiculoId);
            
        } catch (Exception e) {
            System.err.println("❌ Erro processando telemetria: " + e.getMessage());
            // Implementar lógica de retry/DLQ
        }
    }
}