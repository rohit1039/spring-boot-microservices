package com.services.orderservice.proxy.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.orderservice.exception.CustomException;
import com.services.orderservice.exception.ExceptionInResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

        try {
            ExceptionInResponse errorResponse
                = objectMapper.readValue(response.body().asInputStream(),
                ExceptionInResponse.class);

            return new CustomException(errorResponse.getErrorMessage() ,
                errorResponse.getErrorCode(),
                response.status());

        } catch (IOException e) {
            throw  new CustomException("Internal Server Error",
                "INTERNAL_SERVER_ERROR",
                500);
        }
    }
}
