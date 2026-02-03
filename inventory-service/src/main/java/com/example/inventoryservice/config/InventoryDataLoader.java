package com.example.inventoryservice.config;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */


@Component
public class InventoryDataLoader {

    private final InventoryRepository inventoryRepository;

    public InventoryDataLoader(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @PostConstruct
    public void loadInitialInventory() {

        if (inventoryRepository.count() == 0) {

            inventoryRepository.save(
                    Inventory.builder()
                            .productCode("P100")
                            .availableQuantity(100)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .productCode("P200")
                            .availableQuantity(100)
                            .build()
            );

            inventoryRepository.save(
                    Inventory.builder()
                            .productCode("P300")
                            .availableQuantity(50)
                            .build()
            );
        }
    }
}

