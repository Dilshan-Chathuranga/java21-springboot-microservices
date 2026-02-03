package com.example.inventoryservice.exception;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InsufficientStockException class
 */

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productCode) {
        super("Insufficient stock for product: " + productCode);
    }
}
