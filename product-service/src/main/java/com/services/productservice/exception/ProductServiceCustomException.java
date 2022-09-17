package com.services.productservice.exception;

import lombok.Data;

/**
 * @author - ROHIT PARIDA
 */
@Data
public class ProductServiceCustomException extends RuntimeException
{
    private String errorCode;

    /**
     *
     * @param message
     * @param errorCode
     */
    public ProductServiceCustomException(String message, String errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }
}
