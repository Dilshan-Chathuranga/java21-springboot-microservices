package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody OrderRequest request
    ) {
        Order order = service.placeOrder(request);

        if (order.getStatus() == OrderStatus.FAILED) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(order);
        }

        return ResponseEntity.ok(order);
    }
}