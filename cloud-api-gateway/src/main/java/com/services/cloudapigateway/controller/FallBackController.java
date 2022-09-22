package com.services.cloudapigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController
{
    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallBack()
    {
        return "ORDER-SERVICE is down!";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack()
    {
        return "PRODUCT-SERVICE is down!";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack()
    {
        return "PAYMENT-SERVICE is down!";
    }

}
