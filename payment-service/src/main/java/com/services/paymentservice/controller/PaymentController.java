package com.services.paymentservice.controller;

import com.services.paymentservice.model.TransactionDetails;
import com.services.paymentservice.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

@RestController
@RequestMapping("/payment")
@Log4j2
public class PaymentController
{
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<TransactionDetails> doPayment(@RequestBody TransactionDetails transactionDetails)
    {
        log.info("Inside doPayment method");
        return new ResponseEntity<>(this.paymentService.doPayment(transactionDetails), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<TransactionDetails> getPaymentDetailsByOrderId(@PathVariable Long orderId)
    {
        log.info("Inside getPaymentDetailsByOrderId method");
        return new ResponseEntity<>(this.paymentService.getPaymentDetailsByOrderId(orderId), HttpStatus.OK);
    }
}
