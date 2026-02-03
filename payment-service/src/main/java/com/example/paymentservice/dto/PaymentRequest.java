package com.example.paymentservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentRequest class
 */

public record PaymentRequest(
        @NotBlank String orderId,
        @NotNull @Positive BigDecimal amount
) {}
