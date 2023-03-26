package com.cinemastudio.cinemastudioapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieRequest {
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    private int minutes;

    private String director;

    private String country;

    private Date premiere;
}
