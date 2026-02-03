package com.example.orderservice.dto;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

public record InventoryRequest(
        String productCode,
        int quantity
) {}