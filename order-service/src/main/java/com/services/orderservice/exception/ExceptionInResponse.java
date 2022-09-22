package com.services.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionInResponse
{
    private String errorMessage;
    private String errorCode;
    private String description;
}
