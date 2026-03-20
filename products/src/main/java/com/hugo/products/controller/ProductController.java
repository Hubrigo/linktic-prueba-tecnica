package com.hugo.products.controller;

import com.hugo.products.dto.ProductPageResponse;
import com.hugo.products.dto.ProductRequest;
import com.hugo.products.dto.ProductResponse;
import com.hugo.products.jsonapi.JsonApiCollectionResponse;
import com.hugo.products.jsonapi.JsonApiMapper;
import com.hugo.products.jsonapi.JsonApiSingleResponse;
import com.hugo.products.jsonapi.ProductAttributes;
import com.hugo.products.model.ProductStatus;
import com.hugo.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<JsonApiSingleResponse<ProductAttributes>> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonApiMapper.toSingleProductResponse(response));
    }

    @GetMapping
    public ResponseEntity<JsonApiCollectionResponse<ProductAttributes>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        ProductPageResponse response = productService.findAll(status, search, page, size, sortBy, direction);
        return ResponseEntity.ok(JsonApiMapper.toProductCollectionResponse(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonApiSingleResponse<ProductAttributes>> findById(@PathVariable UUID id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok(JsonApiMapper.toSingleProductResponse(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JsonApiSingleResponse<ProductAttributes>> update(@PathVariable UUID id,
                                                                           @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.update(id, request);
        return ResponseEntity.ok(JsonApiMapper.toSingleProductResponse(product));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(@PathVariable UUID id) {
        productService.validateExists(id);
        return ResponseEntity.noContent().build();
    }

}