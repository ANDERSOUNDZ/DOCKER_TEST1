package com.bankfy.bank_meet.domain.models;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BaseResponse<T> {
    private String message;
    private T data;
    private int status;
    private LocalDateTime timestamp;
}