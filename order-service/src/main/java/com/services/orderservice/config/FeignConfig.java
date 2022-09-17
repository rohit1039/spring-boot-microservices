package com.services.orderservice.config;

import com.services.orderservice.proxy.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author - ROHIT PARIDA
 */
@Configuration
public class FeignConfig
{
    /**
     *
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder()
    {
        return new CustomErrorDecoder();
    }
}
