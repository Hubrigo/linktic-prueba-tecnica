package com.hugo.inventory.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiErrorSource {

    private String pointer;

    public JsonApiErrorSource() {
    }

    public JsonApiErrorSource(String pointer) {
        this.pointer = pointer;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }
}