package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.orderservice.client.InventoryClient;

import java.time.Instant;
import java.util.Optional;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    public OrderService(
            OrderRepository orderRepository,
            InventoryClient inventoryClient,
            PaymentClient paymentClient
    ) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
    }

    public Order placeOrder(OrderRequest request) {


        log.info("Placing order | product={} qty={} amount={}",
                request.productCode(), request.quantity(), request.amount());

        Order order = Order.builder()
                .productCode(request.productCode())
                .quantity(request.quantity())
                .amount(request.amount())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        orderRepository.save(order);

        try {
            log.info("Reserving inventory for orderId={}", order.getId());
            inventoryClient.reserve(
                    request.productCode(),
                    request.quantity()
            );

            paymentClient.processPayment(
                    order.getId(),
                    request.amount()
            );

            order.setStatus(OrderStatus.COMPLETED);
            log.info("Order COMPLETED | orderId={}", order.getId());
        } catch (Exception ex) {
            log.error("Order FAILED | orderId={} | reason={}",
                    order.getId(), ex.getMessage());
            inventoryClient.release(
                    request.productCode(),
                    request.quantity()

            );

            order.setStatus(OrderStatus.FAILED);
        }

        return orderRepository.save(order);
    }
}


