package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
}