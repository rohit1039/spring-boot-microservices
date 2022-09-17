package com.services.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse
{
    private LocalDateTime timestamp;
    private String errorMessage;
    private String errorCode;
    private int statusCode;
}
