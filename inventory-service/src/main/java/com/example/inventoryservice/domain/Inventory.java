package com.example.inventoryservice.domain;


import com.example.inventoryservice.exception.InsufficientStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: Inventory class
 */
@Entity
@Table(name ="inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private int availableQuantity;

    public void reserve(int quantity) {
        if (availableQuantity < quantity) {
            throw new InsufficientStockException(productCode);
        }
        this.availableQuantity -= quantity;
    }

    public void release(int quantity) {
        this.availableQuantity += quantity;
    }
}
