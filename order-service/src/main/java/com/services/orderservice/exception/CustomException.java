package com.services.orderservice.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException
{
    private String errorCode;
    private int statusCode;

    public CustomException(String errorMessage, String errorCode, int statusCode)
    {
        super(errorMessage);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
