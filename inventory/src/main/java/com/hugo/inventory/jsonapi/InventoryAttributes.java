package com.hugo.inventory.jsonapi;

import com.hugo.inventory.dto.InventoryResponse;

public class InventoryAttributes {

    private String productId;
    private Integer available;

    public static InventoryAttributes from(InventoryResponse response) {
        InventoryAttributes attributes = new InventoryAttributes();
        attributes.setProductId(response.getProductId().toString());
        attributes.setAvailable(response.getAvailable());
        return attributes;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}