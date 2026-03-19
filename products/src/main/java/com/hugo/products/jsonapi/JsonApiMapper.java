package com.hugo.products.jsonapi;

import com.hugo.products.dto.ProductPageResponse;
import com.hugo.products.dto.ProductResponse;

import java.util.List;

public final class JsonApiMapper {

    private JsonApiMapper() {
    }

    public static JsonApiSingleResponse<ProductAttributes> toSingleProductResponse(ProductResponse product) {
        ProductAttributes attributes = ProductAttributes.from(product);

        JsonApiResource<ProductAttributes> resource = new JsonApiResource<>(
                "products",
                product.getId().toString(),
                attributes
        );

        return new JsonApiSingleResponse<>(resource);
    }

    public static JsonApiCollectionResponse<ProductAttributes> toProductCollectionResponse(ProductPageResponse pageResponse) {
        List<JsonApiResource<ProductAttributes>> data = pageResponse.getContent()
                .stream()
                .map(product -> new JsonApiResource<>(
                        "products",
                        product.getId().toString(),
                        ProductAttributes.from(product)
                ))
                .toList();

        JsonApiMeta meta = new JsonApiMeta(
                pageResponse.getPage(),
                pageResponse.getSize(),
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages()
        );

        return new JsonApiCollectionResponse<>(data, meta);
    }
}