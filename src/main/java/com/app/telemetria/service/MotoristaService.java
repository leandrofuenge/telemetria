package com.app.telemetria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.telemetria.entity.Motorista;
import com.app.telemetria.repository.MotoristaRepository;

@Service
public class MotoristaService {

    @Autowired
    private MotoristaRepository repository;

    public Motorista salvar(Motorista motorista) {
        return repository.save(motorista);
    }

    public List<Motorista> listar() {
        return repository.findAll();
    }

    public Motorista buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));
    }

    public Motorista atualizar(Long id, Motorista dados) {
        Motorista motorista = buscarPorId(id);

        motorista.setNome(dados.getNome());
        motorista.setCpf(dados.getCpf());
        motorista.setCnh(dados.getCnh());
        motorista.setCategoriaCnh(dados.getCategoriaCnh());

        return repository.save(motorista);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
