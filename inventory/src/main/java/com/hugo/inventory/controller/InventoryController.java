package com.hugo.inventory.controller;

import com.hugo.inventory.dto.*;
import com.hugo.inventory.jsonapi.InventoryAttributes;
import com.hugo.inventory.jsonapi.JsonApiMapper;
import com.hugo.inventory.jsonapi.JsonApiSingleResponse;
import com.hugo.inventory.jsonapi.PurchaseAttributes;
import com.hugo.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<JsonApiSingleResponse<InventoryAttributes>> create(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonApiMapper.toInventoryResponse(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<JsonApiSingleResponse<InventoryAttributes>> findByProductId(@PathVariable UUID productId) {
        InventoryResponse response = inventoryService.findByProductId(productId);
        return ResponseEntity.ok(JsonApiMapper.toInventoryResponse(response));
    }

    @PostMapping("/purchase")
    public ResponseEntity<JsonApiSingleResponse<PurchaseAttributes>> purchase(
            @Valid @RequestBody PurchaseRequest request,
            @Parameter(description = "Idempotency key to avoid duplicate purchases", required = true)
            @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        PurchaseResponse response = inventoryService.purchase(request, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonApiMapper.toPurchaseResponse(response));
    }
}