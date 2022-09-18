package com.services.orderservice.service;

import com.services.orderservice.dao.OrderRepository;
import com.services.orderservice.entity.Order;
import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.model.OrderDTO;
import com.services.orderservice.proxy.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author - ROHIT PARIDA
 */
@Service
@Log4j2
public class OrderServiceImpl implements OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    /**
     *
     * @param orderDTO
     * @param Id
     * @return
     */
    @Override
    public OrderDTO orderNow(OrderDTO orderDTO, Long Id)
    {
        OrderDTO dto = new OrderDTO();

        productService.reduceQuantity(Id, orderDTO.getQuantity());

        log.info("Placing Order with status: {}", OrderStatus.ORDER_COMPLETED);
        Order order = Order
            .builder()
            .orderDate(LocalDateTime.now())
            .price(orderDTO.getPrice())
            .orderStatus(OrderStatus.ORDER_COMPLETED)
            .paymentMode(PaymentMode.CASH_ON_DELIVERY)
            .totalAmount(orderDTO.getTotalAmount())
            .productId(Id)
            .quantity(orderDTO.getQuantity())
            .build();

        order = this.orderRepository.save(order);
        log.info("Order placed successfully!");
        BeanUtils.copyProperties(order, dto);

        return dto;
    }
}
