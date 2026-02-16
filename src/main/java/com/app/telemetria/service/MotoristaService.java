package com.app.telemetria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.app.telemetria.entity.Motorista;
import com.app.telemetria.exception.MotoristaDuplicateException;
import com.app.telemetria.exception.MotoristaNotFoundException;
import com.app.telemetria.repository.MotoristaRepository;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository repository;

    public Motorista salvar(Motorista motorista) {
        try {
            return repository.save(motorista);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("cpf")) {
                throw new MotoristaDuplicateException("CPF", motorista.getCpf());
            }
            if (e.getMessage().contains("cnh")) {
                throw new MotoristaDuplicateException("CNH", motorista.getCnh());
            }
            throw e;
        }
    }

    public List<Motorista> listar() {
        return repository.findAll();
    }

    public Motorista buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException(id));
    }
    
    public Motorista buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new MotoristaNotFoundException(cpf));
    }

    public Motorista atualizar(Long id, Motorista dados) {
        Motorista motorista = buscarPorId(id);
        
        motorista.setNome(dados.getNome());
        motorista.setCpf(dados.getCpf());
        motorista.setCnh(dados.getCnh());
        motorista.setCategoriaCnh(dados.getCategoriaCnh());
        
        try {
            return repository.save(motorista);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("cpf")) {
                throw new MotoristaDuplicateException("CPF", dados.getCpf());
            }
            if (e.getMessage().contains("cnh")) {
                throw new MotoristaDuplicateException("CNH", dados.getCnh());
            }
            throw e;
        }
    }

    public void deletar(Long id) {
        Motorista motorista = buscarPorId(id);
        repository.delete(motorista);
    }
}