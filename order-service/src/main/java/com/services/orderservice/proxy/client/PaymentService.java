package com.services.orderservice.proxy.client;

import com.services.orderservice.exception.CustomException;
import com.services.orderservice.proxy.model.TransactionDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(url = "${payment.service.url}", name = "${payment.service.name}")
public interface PaymentService
{
    @PostMapping("/payment")
    ResponseEntity<Object> doPayment(@RequestBody TransactionDetails transactionDetails);

    default ResponseEntity<Object> fallback(Exception exception)
    {
        throw new CustomException("Payment Service is not available!", HttpStatus.SERVICE_UNAVAILABLE.toString(), 500);
    }
}
