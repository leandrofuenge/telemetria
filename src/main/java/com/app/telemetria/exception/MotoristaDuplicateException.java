package com.app.telemetria.exception;

public class MotoristaDuplicateException extends RuntimeException {
    public MotoristaDuplicateException(String cpf) {
        super("Já existe um motorista cadastrado com o CPF: " + cpf);
    }
    
    public MotoristaDuplicateException(String campo, String valor) {
        super(String.format("Já existe um motorista cadastrado com %s: %s", campo, valor));
    }
}