package com.bankfy.bank_meet.domain.exceptions;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de duplicados (DataIntegrity) - Usando objetos de error
    // predefinidos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatedData(DataIntegrityViolationException ex) {
        ErrorMessages errorInfo = ErrorMessages.CLIENT_ALREADY_EXISTS;
        HttpStatusCode status = Objects.requireNonNullElse(errorInfo.getStatus(), HttpStatus.CONFLICT);

        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error(errorInfo.getError())
                .message(errorInfo.getMessage())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build(), status);
    }

    // 2. Manejo de validaciones de DTOs (@Valid) - USANDO LAMBDAS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erroresDetallados = new HashMap<>();

        // Programación funcional: Iteramos los errores con forEach (Lambda)
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> erroresDetallados.put(err.getField(), err.getDefaultMessage()));

        ErrorMessages errorInfo = ErrorMessages.INVALID_INPUT;

        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error(errorInfo.getError())
                .message(erroresDetallados)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    // 3. Manejo de errores de negocio (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessError(IllegalArgumentException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Error de validación de negocio")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    // 4. Manejo de excepciones manuales de validación
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleManualValidation(ValidationException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Datos inválidos (Manual)")
                .message(ex.getErrors())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    // 5. Manejo de errores de transacción (JPA/Constraints)
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionException(TransactionSystemException ex) {
        String mensaje = "Error de integridad de datos: Verifique los campos enviados.";
        Throwable cause = ex.getRootCause();
        if (cause instanceof jakarta.validation.ConstraintViolationException) {
            mensaje = "Validación fallida en base de datos: " + cause.getMessage();
        }
        return buildResponse(mensaje, null, HttpStatus.BAD_REQUEST);
    }

    // 6. Manejo de JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonError(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Error de formato JSON")
                .message("El cuerpo de la solicitud no es un JSON válido o tiene valores mal formados.")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    // Helper para BaseResponse
    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String message, T data, HttpStatus status) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .message(message).data(data).status(status.value())
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(response, status);
    }
}