package com.services.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "productId")
    private Long productId;
    @Column(name = "productPrice")
    private Double price;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "orderDate")
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus")
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMode")
    private PaymentMode paymentMode;
    @Column(name = "totalAmount")
    private Double totalAmount;
}
