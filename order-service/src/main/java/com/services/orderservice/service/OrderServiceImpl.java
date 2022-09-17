package com.services.orderservice.service;

import com.services.orderservice.dao.OrderRepository;
import com.services.orderservice.entity.Order;
import com.services.orderservice.entity.OrderStatus;
import com.services.orderservice.entity.PaymentMode;
import com.services.orderservice.model.OrderDTO;
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

        Order order = Order
            .builder()
            .orderDate(LocalDateTime.now())
            .price(orderDTO.getPrice())
            .orderStatus(OrderStatus.ORDERED)
            .paymentMode(PaymentMode.CASH_ON_DELIVERY)
            .totalAmount(orderDTO.getTotalAmount())
            .productId(Id)
            .quantity(orderDTO.getQuantity())
            .build();

        log.info("Placing Order: {}", order);
        order = this.orderRepository.save(order);
        log.info("Order placed successfully!");
        BeanUtils.copyProperties(order, dto);

        return dto;
    }
}
