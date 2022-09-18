package com.services.orderservice.service;

import com.services.orderservice.model.OrderDTO;

public interface OrderService
{
    public OrderDTO orderNow(OrderDTO orderDTO, Long Id);
}
