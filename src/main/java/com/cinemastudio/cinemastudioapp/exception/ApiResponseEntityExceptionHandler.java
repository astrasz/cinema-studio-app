package com.cinemastudio.cinemastudioapp.exception;

import com.cinemastudio.cinemastudioapp.dto.ExceptionContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNofFoundException.class)
    public ResponseEntity<ExceptionContent> handleResourceNotFoundException(ResourceNofFoundException exception, WebRequest request) {
        ExceptionContent exceptionContent = new ExceptionContent(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionContent, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DemandedResourceInvalidException.class)
    public ResponseEntity<ExceptionContent> handleDemandedResourceInvalidException(DemandedResourceInvalidException exception, WebRequest request) {
        ExceptionContent exceptionContent = new ExceptionContent(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionContent, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<ExceptionContent> handleInvalidRequestParameterException(InvalidRequestParameterException exception, WebRequest request) {
        ExceptionContent exceptionContent = new ExceptionContent(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionContent, HttpStatus.BAD_REQUEST);
    }
}
