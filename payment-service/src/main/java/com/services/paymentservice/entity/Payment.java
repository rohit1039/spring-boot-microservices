package com.services.paymentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payment")
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(name = "orderId", nullable = false)
    private Long orderId;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_Mode")
    private PaymentMode paymentMode;
    @Column(name = "referenceNumber", nullable = false)
    private String referenceNumber;
    @Column(name = "payment_Date")
    private Instant paymentDate;
    @Column(name = "payment_Status")
    private String paymentStatus;
    @Column(name = "total_Amount")
    private Double totalAmount;
}
