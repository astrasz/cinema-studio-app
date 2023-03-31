package com.cinemastudio.cinemastudioapp.dto.response;

import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class ShowTimeResponse {

    private String id;

    // new SimpleDateFormat("yyyy-M-dd hh:mm a", Locale.ENGLISH);
    private String date;

    private String movieTitle;

    private List<Map<String, String>> seats;
}
