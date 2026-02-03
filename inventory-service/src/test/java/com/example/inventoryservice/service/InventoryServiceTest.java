package com.example.inventoryservice.service;


import com.example.inventoryservice.domain.Inventory;
import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.exception.InsufficientStockException;
import com.example.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryServiceTest class
 */

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryService service;

    @Test
    void reserve_shouldReduceStock_whenEnoughQuantity() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .productCode("P100")
                .availableQuantity(10)
                .build();

        when(repository.findByProductCode("P100"))
                .thenReturn(Optional.of(inventory));

        service.reserve(new InventoryRequest("P100", 3));

        assertEquals(7, inventory.getAvailableQuantity());
    }

@Test
    void reserve_shouldThrowException_whenInsufficientStock() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .productCode("P100")
                .availableQuantity(2)
                .build();

        when(repository.findByProductCode("P100"))
                .thenReturn(Optional.of(inventory));

        assertThrows(
                InsufficientStockException.class,
                () -> service.reserve(new InventoryRequest("P100", 5))
        );
    }

    @Test
    void release_shouldIncreaseStock() {
        Inventory inventory = Inventory.builder()
                .id(1L)
                .productCode("P100")
                .availableQuantity(5)
                .build();

        when(repository.findByProductCode("P100"))
                .thenReturn(Optional.of(inventory));

        service.release(new InventoryRequest("P100", 2));

        assertEquals(7, inventory.getAvailableQuantity());
    }
}
