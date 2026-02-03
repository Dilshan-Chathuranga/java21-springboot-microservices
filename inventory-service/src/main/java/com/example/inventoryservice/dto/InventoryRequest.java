package com.example.inventoryservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryRequest class
 */

public record InventoryRequest(
        @NotBlank String productCode,
        @Positive int quantity
) {}
