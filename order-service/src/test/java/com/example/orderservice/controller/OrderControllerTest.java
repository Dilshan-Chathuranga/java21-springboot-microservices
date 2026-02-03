package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dilshan Chathuranga
 * @date 2/4/2026
 */

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void placeOrder_shouldReturn200_whenOrderCompleted() throws Exception {

        Order completedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.COMPLETED)
                .build();

        when(service.placeOrder(any()))
                .thenReturn(completedOrder);

        OrderRequest request =
                new OrderRequest("P100", 1, BigDecimal.valueOf(100));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void placeOrder_shouldReturn409_whenOrderFailed() throws Exception {

        Order failedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.FAILED)
                .build();

        when(service.placeOrder(any()))
                .thenReturn(failedOrder);

        OrderRequest request =
                new OrderRequest("P100", 1, BigDecimal.valueOf(100));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
