package com.app.telemetria.exception;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token inválido ou expirado");
    }
}