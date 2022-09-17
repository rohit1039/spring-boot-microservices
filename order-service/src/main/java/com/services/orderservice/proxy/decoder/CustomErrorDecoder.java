package com.services.orderservice.proxy.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.orderservice.exception.CustomException;
import com.services.orderservice.exception.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder
{
    @Override
    public Exception decode(String methodKey, Response response)
    {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try
        {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

            return new CustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());

        } catch (IOException e)
        {
            return new CustomException("Something went wrong on the server!", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
