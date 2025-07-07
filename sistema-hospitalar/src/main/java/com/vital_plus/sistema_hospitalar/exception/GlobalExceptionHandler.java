package com.vital_plus.sistema_hospitalar.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação", erros);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> erros.put(v.getPropertyPath().toString(), v.getMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST, "Violação de restrição", erros);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Entidade não encontrada", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensagem, Object detalhes) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", status.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", mensagem);
        erro.put("detalhes", detalhes);
        return ResponseEntity.status(status).body(erro);
    }
}
