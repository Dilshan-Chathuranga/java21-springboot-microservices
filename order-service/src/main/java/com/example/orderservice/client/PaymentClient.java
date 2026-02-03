package com.example.orderservice.client;

import com.example.orderservice.dto.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */
@Component
public class PaymentClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;
    private final String apiKey;

    public PaymentClient(
            RestTemplate restTemplate,
            @Value("${payment.service.url}") String paymentServiceUrl,
            @Value("${payment.api-key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
        this.apiKey = apiKey;
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public void processPayment(Long orderId, BigDecimal amount) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey);

        Map<String, Object> body = Map.of(
                "orderId", orderId,
                "amount", amount
        );

        restTemplate.postForEntity(
                paymentServiceUrl + "/payments",
                new HttpEntity<>(body, headers),
                Void.class
        );
    }

    public void paymentFallback(Long orderId, BigDecimal amount, Throwable ex) {
        throw new RuntimeException("Payment service unavailable", ex);
    }
}
