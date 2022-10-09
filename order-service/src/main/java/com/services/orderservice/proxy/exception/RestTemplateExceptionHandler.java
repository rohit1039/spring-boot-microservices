package com.services.orderservice.proxy.exception;

import com.services.orderservice.exception.CustomException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RestTemplateExceptionHandler implements ResponseErrorHandler
{
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException
    {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException
    {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError())
        {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody())))
            {
                String httpBodyResponse = reader.lines().collect(Collectors.joining(""));

                String errorMessage = httpBodyResponse;

                throw new CustomException(errorMessage, response.getStatusCode().toString(), response.getRawStatusCode());
            }
        }
    }
}
