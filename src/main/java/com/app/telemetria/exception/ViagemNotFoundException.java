package com.app.telemetria.exception;

public class ViagemNotFoundException extends RuntimeException {
    
    public ViagemNotFoundException(Long id) {
        super("Viagem não encontrada com id: " + id);
    }
    
    public ViagemNotFoundException(String mensagem) {
        super(mensagem);
    }
}