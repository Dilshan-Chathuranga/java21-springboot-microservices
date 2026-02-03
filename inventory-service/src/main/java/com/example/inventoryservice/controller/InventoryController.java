package com.example.inventoryservice.controller;

/**
 * Author: Dilshan Chathuranga
 * Date: 3/2/2026
 * Description: InventoryController class
 */

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {


    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(@Valid @RequestBody InventoryRequest request) {
        service.reserve(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> release(@Valid @RequestBody InventoryRequest request) {
        service.release(request);
        return ResponseEntity.ok().build();
    }
}
