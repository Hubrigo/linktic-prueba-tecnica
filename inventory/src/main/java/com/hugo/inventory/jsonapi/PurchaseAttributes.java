package com.hugo.inventory.jsonapi;

import com.hugo.inventory.dto.PurchaseResponse;

public class PurchaseAttributes {

    private String productId;
    private Integer purchasedQuantity;
    private Integer remainingStock;
    private String message;

    public static PurchaseAttributes from(PurchaseResponse response) {
        PurchaseAttributes attributes = new PurchaseAttributes();

        attributes.setProductId(response.getProductId().toString());
        attributes.setPurchasedQuantity(response.getPurchasedQuantity());
        attributes.setRemainingStock(response.getRemainingStock());
        attributes.setMessage(response.getMessage());

        return attributes;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(Integer purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}