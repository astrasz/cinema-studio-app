package com.cinemastudio.cinemastudioapp.dto.request;

import com.cinemastudio.cinemastudioapp.model.ShowTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SeatRequest {
    @NotEmpty(message = "Hall ids list cannot be empty")
    private List<@NotBlank(message = "Hall id cannot be empty") String> hallsIds;

//    @NotEmpty(message = "Show time id cannot be empty")
//    private String showTimeId;
}
