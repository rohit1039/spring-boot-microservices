package com.services.paymentservice.model;

import com.services.paymentservice.entity.PaymentMode;
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
    private Long orderId;
    private PaymentMode paymentMode;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    private Double totalAmount;
}
