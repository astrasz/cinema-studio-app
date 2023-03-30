package com.cinemastudio.cinemastudioapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Builder
@Data
public class AddApiUserRequest {
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @UniqueElements
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Roles list cannot be empty")
    private List<String> roles;
}
