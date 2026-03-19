package com.hugo.products.jsonapi;

import com.hugo.products.dto.ProductResponse;
import com.hugo.products.model.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductAttributes {

    private String sku;
    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductAttributes from(ProductResponse product) {
        ProductAttributes attributes = new ProductAttributes();
        attributes.setSku(product.getSku());
        attributes.setName(product.getName());
        attributes.setPrice(product.getPrice());
        attributes.setStatus(product.getStatus());
        attributes.setCreatedAt(product.getCreatedAt());
        attributes.setUpdatedAt(product.getUpdatedAt());
        return attributes;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}