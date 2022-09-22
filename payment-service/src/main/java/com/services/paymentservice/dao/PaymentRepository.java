package com.services.paymentservice.dao;

import com.services.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long>
{
  Payment findByOrderId(Long orderId);
}
