package com.vital_plus.sistema_hospitalar.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validações de DTO com @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", HttpStatus.BAD_REQUEST.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", "Erro de validação");

        Map<String, String> detalhes = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> detalhes.put(fieldError.getField(), fieldError.getDefaultMessage()));

        erro.put("detalhes", detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    // Validações com @NotNull, @Min etc. em parâmetros
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", HttpStatus.BAD_REQUEST.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", "Violação de restrição");

        Map<String, String> detalhes = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(violation -> detalhes.put(violation.getPropertyPath().toString(), violation.getMessage()));

        erro.put("detalhes", detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    // Quando uma entidade não é encontrada no banco
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", HttpStatus.NOT_FOUND.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", "Recurso não encontrado");
        erro.put("detalhes", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Acesso negado por falta de permissão
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", HttpStatus.FORBIDDEN.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", "Acesso negado");
        erro.put("detalhes", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // Outros erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        erro.put("timestamp", LocalDateTime.now());
        erro.put("mensagem", "Erro interno no servidor");
        erro.put("detalhes", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
