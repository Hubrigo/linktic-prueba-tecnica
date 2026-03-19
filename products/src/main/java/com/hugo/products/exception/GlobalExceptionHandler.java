package com.hugo.products.exception;

import com.hugo.products.jsonapi.JsonApiError;
import com.hugo.products.jsonapi.JsonApiErrorResponse;
import com.hugo.products.jsonapi.JsonApiErrorSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<JsonApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .toList();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new JsonApiErrorResponse(errors));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<JsonApiErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        HttpStatusCode statusCode = ex.getStatusCode();

        JsonApiError error = new JsonApiError(
                String.valueOf(statusCode.value()),
                resolveTitle(statusCode.value()),
                ex.getReason() != null ? ex.getReason() : "Unexpected error"
        );

        return ResponseEntity.status(statusCode)
                .body(new JsonApiErrorResponse(List.of(error)));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<JsonApiErrorResponse> handleErrorResponse(ErrorResponseException ex) {
        HttpStatusCode statusCode = ex.getStatusCode();

        String detail = ex.getBody() != null && ex.getBody().getDetail() != null
                ? ex.getBody().getDetail()
                : "Unexpected error";

        JsonApiError error = new JsonApiError(
                String.valueOf(statusCode.value()),
                resolveTitle(statusCode.value()),
                detail
        );

        return ResponseEntity.status(statusCode)
                .body(new JsonApiErrorResponse(List.of(error)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiErrorResponse> handleGeneric(Exception ex) {
        JsonApiError error = new JsonApiError(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Internal Server Error",
                "An unexpected error occurred"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JsonApiErrorResponse(List.of(error)));
    }

    private JsonApiError toValidationError(FieldError fieldError) {
        return new JsonApiError(
                String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                "Validation Error",
                fieldError.getDefaultMessage(),
                new JsonApiErrorSource("/data/attributes/" + fieldError.getField())
        );
    }

    private String resolveTitle(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal Server Error";
            case 503 -> "Service Unavailable";
            default -> "Error";
        };
    }
}