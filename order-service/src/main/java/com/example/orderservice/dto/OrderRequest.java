package com.example.orderservice.dto;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;

import java.math.BigDecimal;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */


public record OrderRequest(
        String productCode,
        int quantity,
        BigDecimal amount
) {
    public Order toEntity() {
        return Order.builder()
                .productCode(productCode)
                .quantity(quantity)
                .amount(amount)
                .status(OrderStatus.CREATED)
                .build();
    }
}
