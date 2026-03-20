package com.hugo.inventory.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiError {

    private String status;
    private String title;
    private String detail;
    private JsonApiErrorSource source;

    public JsonApiError() {
    }

    public JsonApiError(String status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public JsonApiError(String status, String title, String detail, JsonApiErrorSource source) {
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public JsonApiErrorSource getSource() {
        return source;
    }

    public void setSource(JsonApiErrorSource source) {
        this.source = source;
    }
}