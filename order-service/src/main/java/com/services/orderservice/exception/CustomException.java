package com.services.orderservice.exception;

import lombok.Data;

/**
 * @author - ROHIT PARIDA
 */
@Data
public class CustomException extends RuntimeException
{
    private String errorCode;
    private int statusCode;

    /**
     *
     * @param errorMessage
     * @param errorCode
     * @param statusCode
     */
    public CustomException(String errorMessage, String errorCode, int statusCode)
    {
        super(errorMessage);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
