package com.app.telemetria.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Login ou senha inválidos");
    }
}