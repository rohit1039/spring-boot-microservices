package com.services.paymentservice.service;

import com.services.paymentservice.dao.PaymentRepository;
import com.services.paymentservice.entity.Payment;
import com.services.paymentservice.entity.PaymentMode;
import com.services.paymentservice.exception.CustomException;
import com.services.paymentservice.model.TransactionDetails;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService
{
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransactionDetails doPayment(TransactionDetails transactionDetails)
    {
        log.info("Recording Transaction Details: {}", transactionDetails);
        Payment payment = Payment.builder()
                .paymentId(transactionDetails.getPaymentId())
                .paymentMode(transactionDetails.getPaymentMode())
                .paymentDate(transactionDetails.getPaymentDate())
                .paymentStatus(transactionDetails.getPaymentStatus())
                .orderId(transactionDetails.getOrderId())
                .referenceNumber(transactionDetails.getReferenceNumber())
                .totalAmount(transactionDetails.getTotalAmount())
                .build();

        Payment savePayment = this.paymentRepository.save(payment);

        log.info("Transaction completed: {}", savePayment.getPaymentId());

        this.modelMapper.map(savePayment, TransactionDetails.class);

        return transactionDetails;
    }

    @Override
    public TransactionDetails getPaymentDetailsByOrderId(Long orderId)
    {
        log.info("Getting payment details with orderID: {}", orderId);

        Payment getPaymentDetails = this.paymentRepository.findByOrderId(orderId);

        if (getPaymentDetails == null)
        {
            throw new CustomException("Payment details not found with orderId: " + orderId, HttpStatus.BAD_REQUEST.toString(), 400);
        }

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentId(getPaymentDetails.getPaymentId())
                .orderId(getPaymentDetails.getOrderId())
                .paymentMode(getPaymentDetails.getPaymentMode())
                .paymentStatus(getPaymentDetails.getPaymentStatus())
                .paymentDate(getPaymentDetails.getPaymentDate())
                .referenceNumber(getPaymentDetails.getReferenceNumber())
                .totalAmount(getPaymentDetails.getTotalAmount())
                .build();

        return transactionDetails;
    }
}
