package com.app.telemetria.repository;

import com.app.telemetria.entity.Alerta;
import com.app.telemetria.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    
    List<Alerta> findByVeiculoOrderByDataHoraDesc(Veiculo veiculo);
    
    List<Alerta> findByMotoristaIdOrderByDataHoraDesc(Long motoristaId);
    
    List<Alerta> findByViagemIdOrderByDataHoraDesc(Long viagemId);
    
    List<Alerta> findByResolvidoFalseOrderByDataHoraDesc();
    
    List<Alerta> findByGravidadeAndResolvidoFalseOrderByDataHoraDesc(String gravidade);
    
    @Query("SELECT a FROM Alerta a WHERE a.veiculo = :veiculo AND a.tipo = :tipo AND a.resolvido = false ORDER BY a.dataHora DESC")
    Optional<Alerta> findPrimeiroByVeiculoAndTipoOrderByDataHoraDesc(
        @Param("veiculo") Veiculo veiculo, 
        @Param("tipo") String tipo);
    
    boolean existsByVeiculoAndTipoAndResolvidoFalse(Veiculo veiculo, String tipo);
    
    List<Alerta> findByVeiculoAndTipoAndResolvidoFalseOrderByDataHoraDesc(
        Veiculo veiculo, String tipo);
    
    @Query("SELECT a FROM Alerta a WHERE a.dataHora BETWEEN :inicio AND :fim ORDER BY a.dataHora DESC")
    List<Alerta> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    long countByResolvidoFalse();
    
    long countByGravidadeAndResolvidoFalse(String gravidade);
}