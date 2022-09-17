package com.services.orderservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService
{
    @PutMapping("/reduce-quantity/{productId}")
    ResponseEntity<Object> reduceQuantity(@PathVariable Long productId, @RequestParam Long quantity);
}
