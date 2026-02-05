package com.bankfy.bank_meet.infrastructure.adapters.input.exceptions;

import com.bankfy.bank_meet.domain.exceptions.ErrorMessages;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.error.ApiErrorResponse;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erroresDetallados = new HashMap<>();

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessError(IllegalArgumentException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Error de validación de negocio")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleManualValidation(ValidationException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Datos inválidos (Manual)")
                .message(ex.getErrors())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<BaseResponse<String>> handleTransactionException(TransactionSystemException ex) {
        String mensaje = "Error de integridad de datos JPA: Verifique los campos enviados.";
        Throwable cause = ex.getRootCause();
        if (cause instanceof jakarta.validation.ConstraintViolationException) {
            mensaje = "Validación fallida en base de datos: " + cause.getMessage();
        }
        return buildResponse(mensaje, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonError(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Error de formato JSON")
                .message("El cuerpo de la solicitud no es un JSON válido o tiene valores mal formados.")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaughtException(Exception ex) {

        return new ResponseEntity<>(ApiErrorResponse.builder()
                .error("Error interno del servidor")
                .message("Ocurrió un error inesperado. Por favor, contacte al administrador.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String message, T data, HttpStatus status) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .message(message).data(data).status(status.value())
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(response, status);
    }
}