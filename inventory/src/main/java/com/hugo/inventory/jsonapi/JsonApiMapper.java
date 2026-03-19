package com.hugo.inventory.jsonapi;

import com.hugo.inventory.dto.InventoryResponse;
import com.hugo.inventory.dto.PurchaseResponse;

public final class JsonApiMapper {

    private JsonApiMapper() {
    }

    public static JsonApiSingleResponse<InventoryAttributes> toInventoryResponse(InventoryResponse response) {
        JsonApiResource<InventoryAttributes> resource = new JsonApiResource<>(
                "inventories",
                response.getProductId().toString(),
                InventoryAttributes.from(response)
        );

        return new JsonApiSingleResponse<>(resource);
    }

    public static JsonApiSingleResponse<PurchaseAttributes> toPurchaseResponse(PurchaseResponse response) {

        JsonApiResource<PurchaseAttributes> resource = new JsonApiResource<>(
                "purchases",
                response.getProductId().toString(),
                PurchaseAttributes.from(response)
        );

        return new JsonApiSingleResponse<>(resource);
    }
}