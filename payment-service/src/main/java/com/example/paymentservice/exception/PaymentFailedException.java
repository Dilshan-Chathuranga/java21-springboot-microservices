package com.example.paymentservice.exception;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentFailedException class
 */

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
