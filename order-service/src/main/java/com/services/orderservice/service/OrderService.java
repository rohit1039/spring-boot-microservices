package com.services.orderservice.service;

import com.services.orderservice.model.OrderDTO;

public interface OrderService
{
    OrderDTO orderNow(OrderDTO orderDTO, Long Id);

    OrderDTO getOrderById(Long orderId);
}
