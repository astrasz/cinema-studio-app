package com.cinemastudio.cinemastudioapp.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class ValidationExceptionDetails {
    private HttpStatus status;
    private Date date;
    private List<String> errors;

    public ValidationExceptionDetails(HttpStatus status, Date date, List<String> errors) {
        super();
        this.status = status;
        this.date = date;
        this.errors = errors;
    }

    public ValidationExceptionDetails(HttpStatus status, Date date, String error) {
        super();
        this.status = status;
        this.date = date;
        errors = Arrays.asList(error);
    }
}
