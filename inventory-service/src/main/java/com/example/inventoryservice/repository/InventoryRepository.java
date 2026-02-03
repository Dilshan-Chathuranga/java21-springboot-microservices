package com.example.inventoryservice.repository;


import com.example.inventoryservice.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryRepository class
 */

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductCode(String productCode);
}
