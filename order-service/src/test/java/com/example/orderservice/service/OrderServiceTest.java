package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    // Mock downstream services
    @MockitoBean
    private InventoryClient inventoryClient;

    @MockitoBean
    private PaymentClient paymentClient;

    @Autowired
    private OrderRepository orderRepository;

    // ---------------- SUCCESS SCENARIO ----------------

    @Test
    void placeOrder_success() {

        // Inventory + Payment succeed
        doNothing().when(inventoryClient)
                .reserve(anyString(), anyInt());

        doNothing().when(paymentClient)
                .processPayment(any(), any());

        OrderRequest request =
                new OrderRequest("P100", 2, BigDecimal.valueOf(100));

        Order order = orderService.placeOrder(request);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.COMPLETED, order.getStatus());

        verify(inventoryClient, times(1))
                .reserve("P100", 2);

        verify(paymentClient, times(1))
                .processPayment(order.getId(), BigDecimal.valueOf(100));

        verify(inventoryClient, never())
                .release(anyString(), anyInt());
    }

    // ---------------- FAILURE + COMPENSATION ----------------

    @Test
    void placeOrder_paymentFails_shouldCompensateInventory() {

        // Inventory succeeds
        doNothing().when(inventoryClient)
                .reserve(anyString(), anyInt());

        // Payment fails (CB / exception)
        doThrow(new RuntimeException("Payment service down"))
                .when(paymentClient)
                .processPayment(any(), any());

        OrderRequest request =
                new OrderRequest("P200", 1, BigDecimal.valueOf(50));

        Order order = orderService.placeOrder(request);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.FAILED, order.getStatus());

        verify(inventoryClient, times(1))
                .reserve("P200", 1);

        verify(paymentClient, times(1))
                .processPayment(order.getId(), BigDecimal.valueOf(50));

        // Compensation MUST happen
        verify(inventoryClient, times(1))
                .release("P200", 1);
    }
}
