package com.bankfy.bank_meet.domain.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorMessages {
    CLIENT_ALREADY_EXISTS("Conflicto de datos", "La identificación o el ID de cliente ya existen en el sistema.",
            HttpStatus.CONFLICT),
    INVALID_INPUT("Datos inválidos", "Los datos proporcionados no cumplen con el formato requerido.",
            HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND("No encontrado", "La cuenta solicitada no existe.", HttpStatus.NOT_FOUND);

    private final String error;
    private final String message;
    private final HttpStatus status;

    ErrorMessages(String error, String message, HttpStatus status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }
}