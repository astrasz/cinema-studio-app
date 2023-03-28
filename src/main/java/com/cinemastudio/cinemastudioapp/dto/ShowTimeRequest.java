package com.cinemastudio.cinemastudioapp.dto;

import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowTimeRequest {

    // new SimpleDateFormat("yyyy-M-dd hh:mm a", Locale.ENGLISH);
    @NotEmpty(message = "Date cannot be empty")
    private String date;

    @NotEmpty(message = "Movie id cannot be empty")
    private String movieId;
}
