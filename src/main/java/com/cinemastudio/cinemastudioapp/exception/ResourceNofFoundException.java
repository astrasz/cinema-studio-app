package com.cinemastudio.cinemastudioapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNofFoundException extends RuntimeException {
    private final String resource;
    private final String field;
    private final String fieldValue;

    public ResourceNofFoundException(String resource, String field, String fieldValue) {
        super(String.format("%s with %s %s not found", resource, field, fieldValue));
        this.resource = resource;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public String getFieldValue() {
        return fieldValue;
    }

}
