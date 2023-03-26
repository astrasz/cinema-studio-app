package com.cinemastudio.cinemastudioapp.dto;

import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class MovieResponse {
    private String id;
    private String title;
    private int minutes;

    private String director;

    private String country;

    private Date premiere;

    private List<Date> showTimes;
}
