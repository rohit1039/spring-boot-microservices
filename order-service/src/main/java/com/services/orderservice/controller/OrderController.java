package com.services.orderservice.controller;

import com.services.orderservice.model.OrderDTO;
import com.services.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('Customer')")
    @PostMapping("/product/{productId}")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO, @PathVariable("productId") Long Id)
    {
        log.info("Inside placeOrder method...");

        OrderDTO dto = this.orderService.orderNow(orderDTO, Id);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId)
    {
        log.info("Inside getOrderById method");

        OrderDTO dto = this.orderService.getOrderById(orderId);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
