package com.services.orderservice.service;

import com.services.orderservice.model.OrderDTO;

/**
 * @author - ROHIT PARIDA
 */
public interface OrderService
{
    /**
     *
     * @param orderDTO
     * @param Id
     * @return
     */
    public OrderDTO orderNow(OrderDTO orderDTO,Long Id);
}
