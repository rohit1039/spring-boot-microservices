package com.services.orderservice.proxy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.services.orderservice.entity.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails
{
    private Long paymentId;
    @JsonIgnore
    private Long orderId;
    @JsonIgnore
    private PaymentMode paymentMode;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    @JsonIgnore
    private Double totalAmount;
}
