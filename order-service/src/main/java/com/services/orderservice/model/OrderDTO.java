package com.services.orderservice.model;

import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.proxy.model.ProductDTO;
import com.services.orderservice.proxy.model.TransactionDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO
{
    private long id;
    private Double price;
    @NotNull
    private long quantity;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private PaymentMode paymentMode;
    private double totalAmount;
    private ProductDTO productDTO;
    private TransactionDetails transactionDetails;
}
