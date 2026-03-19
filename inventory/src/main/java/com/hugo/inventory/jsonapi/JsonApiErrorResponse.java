package com.hugo.inventory.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiErrorResponse {

    private List<JsonApiError> errors;

    public JsonApiErrorResponse() {
    }

    public JsonApiErrorResponse(List<JsonApiError> errors) {
        this.errors = errors;
    }

    public List<JsonApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<JsonApiError> errors) {
        this.errors = errors;
    }
}