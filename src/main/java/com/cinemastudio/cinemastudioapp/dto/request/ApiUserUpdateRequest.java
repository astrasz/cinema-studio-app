package com.cinemastudio.cinemastudioapp.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ApiUserUpdateRequest {

    @NotEmpty(message = "Firstname cannot be empty")
    private String firstname;

    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;

    @NotEmpty(message = "Email cannot be empty")
    private String email;
}
