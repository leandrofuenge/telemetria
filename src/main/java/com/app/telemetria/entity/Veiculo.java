package com.app.telemetria.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    private String modelo;
    private Double capacidadeCarga;

    @OneToMany(mappedBy = "veiculo", fetch = FetchType.LAZY)
    private List<Telemetria> historicoTelemetria = new ArrayList<>();

    public Veiculo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Double getCapacidadeCarga() { return capacidadeCarga; }
    public void setCapacidadeCarga(Double capacidadeCarga) { this.capacidadeCarga = capacidadeCarga; }

    public List<Telemetria> getHistoricoTelemetria() { return historicoTelemetria; }
    public void setHistoricoTelemetria(List<Telemetria> historicoTelemetria) { this.historicoTelemetria = historicoTelemetria; }
}
