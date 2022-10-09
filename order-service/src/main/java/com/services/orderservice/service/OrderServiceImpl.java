package com.services.orderservice.service;

import com.services.orderservice.dao.OrderRepository;
import com.services.orderservice.entity.Order;
import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.exception.CustomException;
import com.services.orderservice.exception.ExceptionInResponse;
import com.services.orderservice.model.OrderDTO;
import com.services.orderservice.proxy.model.ProductDTO;
import com.services.orderservice.proxy.model.TransactionDetails;
import com.services.orderservice.proxy.service.PaymentService;
import com.services.orderservice.proxy.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OrderDTO orderNow(OrderDTO orderDTO, Long Id)
    {
        OrderDTO dto = new OrderDTO();

        log.info("Calling Product Service...");

        productService.reduceQuantity(Id, orderDTO.getQuantity());

        log.info("Placing Order with status: {}", OrderStatus.ORDER_CREATED);

        Order order = Order
                .builder()
                .orderDate(LocalDateTime.now())
                .price(orderDTO.getPrice())
                .orderStatus(OrderStatus.ORDER_CREATED)
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .totalAmount(orderDTO.getPrice() * orderDTO.getQuantity())
                .productId(Id)
                .quantity(orderDTO.getQuantity())
                .build();

        order = this.orderRepository.save(order);
        BeanUtils.copyProperties(order, dto);

        log.info("Calling Payment Service to complete the transaction...");
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .orderId(order.getId())
                .referenceNumber(UUID.randomUUID().toString())
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .totalAmount(order.getTotalAmount()).build();
        try
        {
            this.paymentService.doPayment(transactionDetails);
            log.info("Transaction done successfully");
            log.info("Order status updated: {}",OrderStatus.ORDER_PLACED);
            order.setOrderStatus(OrderStatus.ORDER_PLACED);
        } catch (Exception e)
        {
            log.error("Transaction failed due to some error.Please try again!");
            order.setOrderStatus(OrderStatus.ORDER_FAILED);
        }

        order.setOrderStatus(order.getOrderStatus());
        order = this.orderRepository.save(order);

        log.info("Order placed successfully!");
        BeanUtils.copyProperties(order, dto);

        return dto;
    }

    @Override
    public OrderDTO getOrderById(Long orderId)
    {
        log.info("Get order details with ID: " + orderId);
        Order order = this.orderRepository.findById(orderId).orElseThrow(() ->
                (
                        new CustomException("Cannot find order with orderId: " + orderId, HttpStatus.NOT_FOUND.toString(), 404)
                ));

        log.info("Invoking product service to fetch the product details...");

        ProductDTO productDTO = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductDTO.class
        );

        OrderDTO setProductDetails = OrderDTO.builder().productDTO(ProductDTO.builder().productName(productDTO.getProductName()).productId(productDTO.getProductId()).build()).build();

        TransactionDetails transactionDetails = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/" + orderId, TransactionDetails.class
        );

        OrderDTO setPaymentDetails = OrderDTO.builder().transactionDetails(TransactionDetails.builder()
                .paymentId(transactionDetails.getPaymentId())
                .paymentMode(transactionDetails.getPaymentMode())
                .paymentDate(transactionDetails.getPaymentDate())
                .referenceNumber(transactionDetails.getReferenceNumber())
                .paymentStatus(transactionDetails.getPaymentStatus())
                .build()).build();

        OrderDTO dto = OrderDTO.builder()
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentMode(order.getPaymentMode())
                .id(orderId)
                .orderDate(order.getOrderDate())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .productDTO(setProductDetails.getProductDTO())
                .transactionDetails(setPaymentDetails.getTransactionDetails())
                .build();

        return dto;
    }
}
