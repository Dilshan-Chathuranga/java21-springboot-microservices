package com.example.paymentservice.controller;

import com.example.paymentservice.config.SecurityProperties;
import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.exception.PaymentFailedException;
import com.example.paymentservice.security.SecurityConfig;
import com.example.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: PaymentControllerTest class
 */

@WebMvcTest(PaymentController.class)
@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})
@EnableConfigurationProperties(SecurityProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
@TestPropertySource(properties = {
        "app.security.api-key=payment-secret-key"
})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void pay_shouldReturn401_whenApiKeyMissing() throws Exception {
        PaymentRequest request =
                new PaymentRequest("ORD-1", BigDecimal.valueOf(100));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void pay_shouldReturn200_whenPaymentSucceeds() throws Exception {
        PaymentRequest request =
                new PaymentRequest("ORD-1", BigDecimal.valueOf(100));

        doNothing().when(service).process(request);

        mockMvc.perform(post("/payments")
                        .header("X-API-KEY", "payment-secret-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void pay_shouldReturn402_whenPaymentFails() throws Exception {
        PaymentRequest request =
                new PaymentRequest("ORD-1", BigDecimal.valueOf(1500));

        doThrow(new PaymentFailedException("Payment declined"))
                .when(service).process(request);

        mockMvc.perform(post("/payments")
                        .header("X-API-KEY", "payment-secret-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isPaymentRequired());
    }
}
