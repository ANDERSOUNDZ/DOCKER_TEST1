package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.error;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private String error;
    private Object message;
    private int status;
    private LocalDateTime timestamp;
}