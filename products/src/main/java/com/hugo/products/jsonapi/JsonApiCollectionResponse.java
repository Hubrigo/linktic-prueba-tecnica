package com.hugo.products.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiCollectionResponse<T> {

    private List<JsonApiResource<T>> data;
    private JsonApiMeta meta;

    public JsonApiCollectionResponse() {
    }

    public JsonApiCollectionResponse(List<JsonApiResource<T>> data, JsonApiMeta meta) {
        this.data = data;
        this.meta = meta;
    }

    public List<JsonApiResource<T>> getData() {
        return data;
    }

    public void setData(List<JsonApiResource<T>> data) {
        this.data = data;
    }

    public JsonApiMeta getMeta() {
        return meta;
    }

    public void setMeta(JsonApiMeta meta) {
        this.meta = meta;
    }
}