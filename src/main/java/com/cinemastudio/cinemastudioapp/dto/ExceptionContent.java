package com.cinemastudio.cinemastudioapp.dto;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ExceptionContent {
    private final Date date;
    private final String message;
    private final String path;

    public ExceptionContent(Date date, String message, String description) {
        this.date = date;
        this.message = message;
        this.path = description;
    }

    public Date getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
