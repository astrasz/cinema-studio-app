package com.cinemastudio.cinemastudioapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestParameterException extends RuntimeException {
    private final String parameter;
    private final String parameterValue;

    public InvalidRequestParameterException(String parameter, String parameterValue) {
        super(String.format("Value %s of parameter %s is invalid", parameterValue, parameter));
        this.parameter = parameter;
        this.parameterValue = parameterValue;
    }

    public String getParameter() {
        return parameter;
    }

    public String getParameterValue() {
        return parameterValue;
    }
}
