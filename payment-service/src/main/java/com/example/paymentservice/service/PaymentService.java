package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.exception.PaymentFailedException;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentService class
 */

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    public void process(PaymentRequest request) {

        if (request.amount().intValue() > 1000) {
            throw new PaymentFailedException("Payment declined");
        }

        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .build();

        payment.markSuccess();

        repository.save(payment);
    }
}
