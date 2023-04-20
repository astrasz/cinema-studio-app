package com.cinemastudio.cinemastudioapp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieRequest {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Minutes cannot be null")
    private int minutes;

    @NotNull(message = "Director cannot be null")
    private String director;

    @NotNull(message = "Country cannot be null")
    private String country;

    // new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
    @NotNull(message = "Premiere cannot be null")
    private String premiere;

    // String: new SimpleDateFormat("yyyy-M-dd hh:mm a", Locale.ENGLISH);
    @NotNull(message = "Show times cannot be empty")
    private List<String> showTimes;
}
