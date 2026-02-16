package com.app.telemetria.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTANTE

import com.app.telemetria.entity.Rota;
import com.app.telemetria.entity.Veiculo;
import com.app.telemetria.exception.RotaDuplicateException;
import com.app.telemetria.exception.RotaNotFoundException;
import com.app.telemetria.exception.RotaValidationException;
import com.app.telemetria.exception.VeiculoNotFoundException;
import com.app.telemetria.repository.RotaRepository;
import com.app.telemetria.repository.VeiculoRepository;

@Service
public class RotaService {
    
    private final RotaRepository repository;
    private final VeiculoRepository veiculoRepository;
    
    public RotaService(RotaRepository repository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
    }
    
    @Transactional 
    public Rota salvar(Rota rota) {
        // Validações básicas
        if (rota.getNome() == null || rota.getNome().trim().isEmpty()) {
            throw new RotaValidationException("O nome da rota é obrigatório");
        }
        
        if (rota.getOrigem() == null || rota.getDestino() == null) {
            throw new RotaValidationException("Origem e destino são obrigatórios");
        }
        
        try {
            return repository.save(rota);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("nome")) {
                throw new RotaDuplicateException(rota.getNome());
            }
            throw e;
        }
    }
    
    @Transactional(readOnly = true) 
    public List<Rota> listar() {
        return repository.findAll();
    }
    
    @Transactional(readOnly = true) 
    public Rota buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RotaNotFoundException(id));
    }
    
    @Transactional 
    public Rota atualizar(Long id, Rota dados) {
        Rota rota = buscarPorId(id);
        
        if (dados.getNome() != null) {
            rota.setNome(dados.getNome());
        }
        if (dados.getOrigem() != null) {
            rota.setOrigem(dados.getOrigem());
        }
        if (dados.getDestino() != null) {
            rota.setDestino(dados.getDestino());
        }
        if (dados.getLatitudeOrigem() != null) {
            rota.setLatitudeOrigem(dados.getLatitudeOrigem());
        }
        if (dados.getLongitudeOrigem() != null) {
            rota.setLongitudeOrigem(dados.getLongitudeOrigem());
        }
        if (dados.getLatitudeDestino() != null) {
            rota.setLatitudeDestino(dados.getLatitudeDestino());
        }
        if (dados.getLongitudeDestino() != null) {
            rota.setLongitudeDestino(dados.getLongitudeDestino());
        }
        if (dados.getDistanciaPrevista() != null) {
            rota.setDistanciaPrevista(dados.getDistanciaPrevista());
        }
        if (dados.getTempoPrevisto() != null) {
            rota.setTempoPrevisto(dados.getTempoPrevisto());
        }
        if (dados.getAtiva() != null) {
            rota.setAtiva(dados.getAtiva());
        }
        if (dados.getDataInicio() != null) {
            rota.setDataInicio(dados.getDataInicio());
        }
        if (dados.getDataFim() != null) {
            rota.setDataFim(dados.getDataFim());
        }
        
        // Atualiza o veículo se veiculoId foi fornecido
        if (dados.getVeiculo() != null && dados.getVeiculo().getId() != null) {
            Veiculo veiculo = veiculoRepository.findById(dados.getVeiculo().getId())
                .orElseThrow(() -> new VeiculoNotFoundException(dados.getVeiculo().getId()));
            rota.setVeiculo(veiculo);
        }
        
        try {
            return repository.save(rota);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("nome")) {
                throw new RotaDuplicateException(dados.getNome());
            }
            throw e;
        }
    }
    
    @Transactional 
    public void deletar(Long id) {
        Rota rota = buscarPorId(id);
        repository.delete(rota);
    }
}