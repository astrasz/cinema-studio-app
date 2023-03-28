package com.cinemastudio.cinemastudioapp.dto;

import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ShowTimeResponse {

    private String id;

    private LocalDate date;

    private String movieTitle;

    private List<Seat> seats;
}
