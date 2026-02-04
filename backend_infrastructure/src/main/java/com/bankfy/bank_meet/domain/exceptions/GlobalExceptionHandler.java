package com.bankfy.bank_meet.domain.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
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

    // CAPTURA ERROR 409 - CONFLICTO (Duplicados)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatedData(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "La identificación o el ID de cliente ya existen en el sistema.");
        response.put("error", "Conflicto de datos");
        response.put("status", HttpStatus.CONFLICT.value()); // 409
        response.put("timestamp", LocalDateTime.now());
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // CAPTURA ERROR 400 - PETICIÓN INCORRECTA (Validaciones fallidas)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> detalles = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            detalles.put(err.getField(), err.getDefaultMessage()));

        response.put("mensaje", "Los datos enviados no son válidos");
        response.put("error", "Petición incorrecta");
        response.put("detalles", detalles);
        response.put("status", HttpStatus.BAD_REQUEST.value()); // 400
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessError(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", ex.getMessage());
        response.put("error", "Error de validación de negocio");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}