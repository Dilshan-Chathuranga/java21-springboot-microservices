package com.example.paymentservice.service;

import com.example.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.exception.PaymentFailedException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentServiceTest class
 */

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentService service;

    @Test
    void process_shouldThrowException_whenAmountTooHigh() {
        PaymentRequest request =
                new PaymentRequest("ORD-1", BigDecimal.valueOf(1500));

        assertThrows(PaymentFailedException.class,
                () -> service.process(request));
    }

    @Test
    void process_shouldSavePayment_whenValid() {
        PaymentRequest request =
                new PaymentRequest("ORD-1", BigDecimal.valueOf(100));

        service.process(request);

        verify(repository).save(org.mockito.ArgumentMatchers.any());
    }
}
