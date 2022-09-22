package com.services.orderservice.proxy.service;

import com.services.orderservice.exception.CustomException;
import com.services.orderservice.proxy.model.TransactionDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(url = "${payment.service.url}", name = "${payment.service.name}")
public interface PaymentService
{
    @PostMapping("/payment")
    public ResponseEntity<TransactionDetails> doPayment(@RequestBody TransactionDetails transactionDetails);

    @GetMapping("/payment/{orderId}")
    public ResponseEntity<TransactionDetails> getPaymentDetailsByOrderId(@PathVariable Long orderId);

    default void fallback(Exception exception)
    {
        throw new CustomException("Payment Service is not available!", HttpStatus.SERVICE_UNAVAILABLE.toString(), 500);
    }
}
