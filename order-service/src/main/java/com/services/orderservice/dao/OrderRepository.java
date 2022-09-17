package com.services.orderservice.dao;

import com.services.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author - ROHIT PARIDA
 */
public interface OrderRepository extends JpaRepository<Order,Long>
{

}
