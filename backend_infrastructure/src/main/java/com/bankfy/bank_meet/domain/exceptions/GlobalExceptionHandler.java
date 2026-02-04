package com.bankfy.bank_meet.domain.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatedData(DataIntegrityViolationException ex) {
        ErrorMessages errorInfo = ErrorMessages.CLIENT_ALREADY_EXISTS;

        HttpStatusCode status = Objects.requireNonNullElse(errorInfo.getStatus(), HttpStatus.CONFLICT);

        ApiErrorResponse error = ApiErrorResponse.builder()
                .error(errorInfo.getError())
                .message(errorInfo.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessError(IllegalArgumentException ex) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse error = ApiErrorResponse.builder()
                .error("Error de validación de negocio")
                .message(ex.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erroresDetallados = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> erroresDetallados.put(err.getField(), err.getDefaultMessage()));

        ErrorMessages errorInfo = ErrorMessages.INVALID_INPUT;
        HttpStatusCode status = Objects.requireNonNullElse(errorInfo.getStatus(), HttpStatus.BAD_REQUEST);

        ApiErrorResponse error = ApiErrorResponse.builder()
                .error(errorInfo.getError())
                .message(erroresDetallados)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, status);
    }
}