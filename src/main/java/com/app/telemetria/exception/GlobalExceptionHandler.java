package com.app.telemetria.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= HANDLERS DE AUTENTICAÇÃO =================
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Autenticação falhou",
            "Login ou senha inválidos",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(
            NoSuchElementException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado",
            "Usuário não encontrado no sistema",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
            io.jsonwebtoken.JwtException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Token inválido",
            "Token inválido ou expirado",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(
            DisabledException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Conta desabilitada",
            "Sua conta está desabilitada. Contate o administrador.",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(
            LockedException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Conta bloqueada",
            "Sua conta está bloqueada. Contate o administrador.",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // ================= HANDLER GENÉRICO PARA DATA INTEGRITY =================
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        
        String message = "Erro de integridade de dados";
        String errorType = "Violação de constraint";
        HttpStatus status = HttpStatus.CONFLICT;
        
        String exMessage = ex.getMessage();
        
        if (exMessage.contains("Duplicate entry")) {
            if (exMessage.contains("cpf")) {
                message = "CPF já cadastrado no sistema";
                errorType = "CPF duplicado";
            } else if (exMessage.contains("cnh")) {
                message = "CNH já cadastrada no sistema";
                errorType = "CNH duplicada";
            } else if (exMessage.contains("email")) {
                message = "E-mail já cadastrado no sistema";
                errorType = "E-mail duplicado";
            } else if (exMessage.contains("login")) {
                message = "Login já cadastrado no sistema";
                errorType = "Login duplicado";
            } else if (exMessage.contains("placa")) {
                message = "Placa já cadastrada no sistema";
                errorType = "Placa duplicada";
            } else if (exMessage.contains("nome")) {
                message = "Nome já cadastrado no sistema";
                errorType = "Nome duplicado";
            }
        } else if (exMessage.contains("cannot be null")) {
            message = "Campos obrigatórios não preenchidos";
            errorType = "Campo obrigatório";
            status = HttpStatus.BAD_REQUEST;
        } else if (exMessage.contains("foreign key")) {
            message = "Registro possui referências em outras tabelas";
            errorType = "Violação de chave estrangeira";
            status = HttpStatus.CONFLICT;
        }
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            errorType,
            message,
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(status).body(error);
    }

    // ================= HANDLERS DE VALIDAÇÃO =================
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de validação",
            "Campos inválidos",
            request.getDescription(false).replace("uri=", ""),
            errors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ================= HANDLERS DE EXCEÇÕES PERSONALIZADAS =================
    
    @ExceptionHandler(RotaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRotaNotFoundException(
            RotaNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Rota não encontrada",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RotaDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleRotaDuplicateException(
            RotaDuplicateException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Rota duplicada",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RotaValidationException.class)
    public ResponseEntity<ErrorResponse> handleRotaValidationException(
            RotaValidationException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de validação de rota",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MotoristaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMotoristaNotFoundException(
            MotoristaNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Motorista não encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MotoristaDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleMotoristaDuplicateException(
            MotoristaDuplicateException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Motorista duplicado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(VeiculoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVeiculoNotFoundException(
            VeiculoNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Veículo não encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Requisição inválida",
            ex.getMessage() != null ? ex.getMessage() : "Dados inválidos",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // ================= HANDLER GENÉRICO =================
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno",
            "Ocorreu um erro inesperado. Tente novamente mais tarde.",
            request.getDescription(false).replace("uri=", ""),
            null
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}