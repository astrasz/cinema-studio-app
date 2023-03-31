package com.cinemastudio.cinemastudioapp.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse {
    private String id;
    private String title;
    private int minutes;

    private String director;

    private String country;

    private Date premiere;

    private List<String> showTimes;
}
