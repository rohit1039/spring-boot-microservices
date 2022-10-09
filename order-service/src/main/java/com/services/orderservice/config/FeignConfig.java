package com.services.orderservice.config;

import com.services.orderservice.proxy.decoder.CustomErrorDecoder;
import com.services.orderservice.proxy.exception.RestTemplateExceptionHandler;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;

@Configuration
public class FeignConfig
{
    @Bean
    public ErrorDecoder errorDecoder()
    {
        return new CustomErrorDecoder();
    }
}
