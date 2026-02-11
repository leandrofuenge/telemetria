package com.app.telemetria.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "viagens")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @OneToOne
    @JoinColumn(name = "carga_id")
    private Carga carga;

    private LocalDateTime dataSaida;
    private LocalDateTime dataChegadaPrevista;
    private String status; // Ex: EM_TRANSITO, FINALIZADA

    // Construtor Padrão (Obrigatório para o JPA)
    public Viagem() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Carga getCarga() {
        return carga;
    }

    public void setCarga(Carga carga) {
        this.carga = carga;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataChegadaPrevista() {
        return dataChegadaPrevista;
    }

    public void setDataChegadaPrevista(LocalDateTime dataChegadaPrevista) {
        this.dataChegadaPrevista = dataChegadaPrevista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}