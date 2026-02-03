package com.example.orderservice.dto;

import java.math.BigDecimal;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

public record PaymentRequest(
        String orderId,
        BigDecimal amount
) {}
