package com.app.telemetria.service;

import com.app.telemetria.entity.Alerta;
import com.app.telemetria.entity.Motorista;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.entity.Viagem;
import com.app.telemetria.entity.Rota;
import com.app.telemetria.repository.AlertaRepository;
import com.app.telemetria.repository.VeiculoRepository;
import com.app.telemetria.repository.ViagemRepository;
import com.app.telemetria.util.DistanciaCalculator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LocalizacaoValidationService {
    
    private final IPLocateService ipLocateService;
    private final TelemetriaService telemetriaService;
    private final ViagemRepository viagemRepository;
    private final AlertaRepository alertaRepository;
    private final VeiculoRepository veiculoRepository;
    private final DistanciaCalculator distanciaCalculator;
    
    // Limites configuráveis
    private static final double DISTANCIA_MAXIMA_IP_GPS = 50.0; // km
    private static final double DISTANCIA_MAXIMA_IP_ROTA = 100.0; // km
    
    public LocalizacaoValidationService(
            IPLocateService ipLocateService,
            TelemetriaService telemetriaService,
            ViagemRepository viagemRepository,
            AlertaRepository alertaRepository,
            VeiculoRepository veiculoRepository,
            DistanciaCalculator distanciaCalculator) {
        this.ipLocateService = ipLocateService;
        this.telemetriaService = telemetriaService;
        this.viagemRepository = viagemRepository;
        this.alertaRepository = alertaRepository;
        this.veiculoRepository = veiculoRepository;
        this.distanciaCalculator = distanciaCalculator;
    }
    
    /**
     * Valida localização do motorista baseado no IP
     * Chamado quando o motorista faz login ou qualquer requisição
     */
    public void validarLocalizacaoMotorista(Long motoristaId, Long veiculoId, String ip) {
        // 1. Buscar localização por IP
        var localizacaoIP = ipLocateService.buscarLocalizacaoPorIP(ip);
        if (localizacaoIP.isEmpty()) {
            criarAlertaLocalizacao(veiculoId, motoristaId, 
                "LOCALIZACAO_DESCONHECIDA",
                "Não foi possível determinar localização do IP: " + ip,
                "MEDIA");
            return;
        }
        
        var ipInfo = localizacaoIP.get();
        
        // 2. Verificar uso de proxy/VPN
        if (ipInfo.usandoProxy()) {
            criarAlertaLocalizacao(veiculoId, motoristaId,
                "PROXY_DETECTADO",
                String.format("Uso de proxy/VPN detectado: %s - %s/%s", 
                    ip, ipInfo.cidade(), ipInfo.estado()),
                "ALTA");
        }
        
        // 3. Verificar se está no Brasil (assumindo que sua frota opera aqui)
        if (!"Brazil".equalsIgnoreCase(ipInfo.pais()) && 
            !"BR".equalsIgnoreCase(ipInfo.pais())) {
            criarAlertaLocalizacao(veiculoId, motoristaId,
                "ACESSO_EXTERIOR",
                String.format("Acesso de fora do país: %s (%s)", 
                    ipInfo.pais(), ipInfo.cidade()),
                "CRITICA");
        }
        
        // 4. Confrontar com telemetria (última posição GPS)
        confrontarComTelemetria(veiculoId, motoristaId, ipInfo);
        
        // 5. Confrontar com rota ativa
        confrontarComRotaAtiva(veiculoId, motoristaId, ipInfo);
    }
    
    /**
     * Confronta localização IP com última telemetria GPS
     */
    private void confrontarComTelemetria(Long veiculoId, Long motoristaId, 
                                         IPLocateService.LocalizacaoInfo ipInfo) {
        var ultimaTelemetria = telemetriaService.buscarUltimaPorVeiculo(veiculoId);
        
        if (ultimaTelemetria.isEmpty()) {
            // Sem telemetria recente é um alerta médio
            criarAlertaLocalizacao(veiculoId, motoristaId,
                "DISCREPANCIA_LOCALIZACAO",
                "Acesso via IP mas sem telemetria recente do veículo",
                "MEDIA");
            return;
        }
        
        var telemetria = ultimaTelemetria.get();
        double distancia = distanciaCalculator.calcularDistancia(
            ipInfo.latitude(), ipInfo.longitude(),
            telemetria.getLatitude(), telemetria.getLongitude()
        );
        
        if (distancia > DISTANCIA_MAXIMA_IP_GPS) {
            String mensagem = String.format(
                "Discrepância de localização: IP em %s/%s (%.1f,%.1f) mas GPS em (%.1f,%.1f) - Distância: %.1f km",
                ipInfo.cidade(), ipInfo.estado(),
                ipInfo.latitude(), ipInfo.longitude(),
                telemetria.getLatitude(), telemetria.getLongitude(),
                distancia
            );
            
            criarAlertaLocalizacao(veiculoId, motoristaId,
                "DISCREPANCIA_LOCALIZACAO",
                mensagem,
                distancia > 100 ? "ALTA" : "MEDIA"
            );
        }
    }
    
    /**
     * Confronta localização IP com a rota ativa do veículo
     */
    private void confrontarComRotaAtiva(Long veiculoId, Long motoristaId, 
                                        IPLocateService.LocalizacaoInfo ipInfo) {
        // Buscar viagem ativa do veículo
        Optional<Viagem> viagemAtiva = viagemRepository.findByVeiculoIdAndStatus(veiculoId, "EM_ANDAMENTO");
        
        if (viagemAtiva.isEmpty()) {
            return; // Sem viagem ativa, não há rota para comparar
        }
        
        Viagem viagem = viagemAtiva.get();
        Rota rota = viagem.getRota();
        
        // Calcular distância do IP até a rota
        double distancia = distanciaCalculator.calcularDistanciaAteRota(
            ipInfo.latitude(), ipInfo.longitude(),
            rota.getLatitudeOrigem(), rota.getLongitudeOrigem(),
            rota.getLatitudeDestino(), rota.getLongitudeDestino()
        );
        
        if (distancia > DISTANCIA_MAXIMA_IP_ROTA) {
            String mensagem = String.format(
                "Localização inesperada para rota ativa: IP em %s/%s (%.1f,%.1f) está a %.1f km da rota %s",
                ipInfo.cidade(), ipInfo.estado(),
                ipInfo.latitude(), ipInfo.longitude(),
                distancia, rota.getNome()
            );
            
            criarAlertaLocalizacao(veiculoId, motoristaId,
                "LOCALIZACAO_INESPERADA",
                mensagem,
                distancia > 200 ? "ALTA" : "MEDIA"
            );
        }
    }
    
    /**
     * Cria alerta de localização
     */
    private void criarAlertaLocalizacao(Long veiculoId, Long motoristaId, 
                                        String tipo, String mensagem, String gravidade) {
        veiculoRepository.findById(veiculoId).ifPresent(veiculo -> {
            Alerta alerta = new Alerta();
            alerta.setVeiculo(veiculo);
            
            if (motoristaId != null) {
                Motorista motorista = new Motorista();
                motorista.setId(motoristaId);
                alerta.setMotorista(motorista);
            }
            
            alerta.setTipo(tipo);
            alerta.setGravidade(gravidade);
            alerta.setMensagem(mensagem);
            alerta.setDataHora(LocalDateTime.now());
            alerta.setLido(false);
            alerta.setResolvido(false);
            
            alertaRepository.save(alerta);
            System.out.println("📍 [" + gravidade + "] " + mensagem);
        });
    }
}