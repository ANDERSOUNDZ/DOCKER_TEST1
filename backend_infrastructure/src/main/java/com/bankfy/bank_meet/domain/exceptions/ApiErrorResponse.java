package com.bankfy.bank_meet.domain.exceptions;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
