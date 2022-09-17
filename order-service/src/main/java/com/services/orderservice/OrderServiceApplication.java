package com.services.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author - ROHIT PARIDA
 */
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
