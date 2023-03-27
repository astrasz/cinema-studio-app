package com.cinemastudio.cinemastudioapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DemandedResourceInvalidException extends RuntimeException {

    private final String resource;

    public DemandedResourceInvalidException(String resource) {
        super(String.format("Not received %s", resource));
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
