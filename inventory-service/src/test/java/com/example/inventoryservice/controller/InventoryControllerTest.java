package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.security.SecurityConfig;
import com.example.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryControllerTest class
 */

@WebMvcTest(InventoryController.class)
@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})
@AutoConfigureMockMvc(addFilters = true)
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@TestPropertySource(properties = {
        "app.security.api-key=inventory-secret-key"
})
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void reserve_shouldReturn401_whenApiKeyMissing() throws Exception {
        InventoryRequest request = new InventoryRequest("P100", 1);

        mockMvc.perform(post("/inventory/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void reserve_shouldReturn200_whenApiKeyProvided() throws Exception {
        InventoryRequest request = new InventoryRequest("P100", 1);

        doNothing().when(service).reserve(request);

        mockMvc.perform(post("/inventory/reserve")
                        .header("X-API-KEY", "inventory-secret-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
