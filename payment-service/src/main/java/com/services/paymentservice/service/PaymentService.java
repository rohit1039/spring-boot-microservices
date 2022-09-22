package com.services.paymentservice.service;

import com.services.paymentservice.model.TransactionDetails;

public interface PaymentService
{
  TransactionDetails doPayment(TransactionDetails transactionDetails);

  TransactionDetails getPaymentDetailsByOrderId(Long orderId);
}
