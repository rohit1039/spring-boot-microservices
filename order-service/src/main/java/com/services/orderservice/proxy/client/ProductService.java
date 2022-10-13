package com.services.orderservice.proxy.client;

import com.services.orderservice.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(url = "${product.service.url}", name = "${product.service.name}")
public interface ProductService
{
    @PutMapping("/product/reduce-quantity/{productId}")
    ResponseEntity<Object> reduceQuantity(@PathVariable Long productId, @RequestParam Long quantity);

    default ResponseEntity<Object> fallback(Exception exception)
    {
        throw new CustomException("Product Service is not available!", HttpStatus.SERVICE_UNAVAILABLE.toString(), 500);
    }
}
