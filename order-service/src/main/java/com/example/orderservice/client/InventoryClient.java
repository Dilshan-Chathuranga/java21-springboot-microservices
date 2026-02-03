package com.example.orderservice.client;

import com.example.orderservice.dto.InventoryRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Dilshan Chathuranga
 * @date 2/3/2026
 */

@Slf4j
@Component
public class InventoryClient {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;
    private final String apiKey;

    public InventoryClient(
            RestTemplate restTemplate,
            @Value("${inventory.service.url}") String inventoryServiceUrl,
            @Value("${inventory.api-key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.inventoryServiceUrl = inventoryServiceUrl;
        this.apiKey = apiKey;
    }

    @CircuitBreaker(
            name = "inventoryService",
            fallbackMethod = "reserveFallback"
    )
    public void reserve(String productCode, int quantity) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "productCode", productCode,
                "quantity", quantity
        );
        log.info("Calling INVENTORY reserve | product={} qty={}",
                productCode, quantity);
        restTemplate.postForEntity(
                inventoryServiceUrl + "/inventory/reserve",
                new HttpEntity<>(body, headers),
                Void.class
        );
    }

    public void release(String productCode, int quantity) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "productCode", productCode,
                "quantity", quantity
        );
        log.info("Calling INVENTORY release | product={} qty={}",
                productCode, quantity);
        restTemplate.postForEntity(
                inventoryServiceUrl + "/inventory/release",
                new HttpEntity<>(body, headers),
                Void.class
        );
    }

    public void reserveFallback(
            String productCode,
            int quantity,
            Throwable ex
    ) {
        log.error("INVENTORY CB OPEN | fallback triggered", ex);
        throw new RuntimeException("Inventory service unavailable", ex);
    }
}