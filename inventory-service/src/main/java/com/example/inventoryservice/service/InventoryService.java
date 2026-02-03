package com.example.inventoryservice.service;

import com.example.inventoryservice.domain.Inventory;
import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryService class
 */

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public void reserve(InventoryRequest request) {
        Inventory inventory = repository.findByProductCode(request.productCode())
                .orElseThrow(() -> new InsufficientStockException(request.productCode()));

        inventory.reserve(request.quantity());
    }

    public void release(InventoryRequest request) {
        Inventory inventory = repository.findByProductCode(request.productCode())
                .orElseThrow(() -> new InsufficientStockException(request.productCode()));

        inventory.release(request.quantity());
    }

}
