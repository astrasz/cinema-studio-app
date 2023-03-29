package com.cinemastudio.cinemastudioapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedEntityException extends RuntimeException {

    private final String entityClassName;

    public DuplicatedEntityException(String entityClassName) {
        super(String.format("Cannot create a new %s entity, an entity with these properties values already exist", entityClassName));
        this.entityClassName = entityClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }
}
