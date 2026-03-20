package com.hugo.products.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiSingleResponse<T> {

    private JsonApiResource<T> data;

    public JsonApiSingleResponse() {
    }

    public JsonApiSingleResponse(JsonApiResource<T> data) {
        this.data = data;
    }

    public JsonApiResource<T> getData() {
        return data;
    }

    public void setData(JsonApiResource<T> data) {
        this.data = data;
    }
}