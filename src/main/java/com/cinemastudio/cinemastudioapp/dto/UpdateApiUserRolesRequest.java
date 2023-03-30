package com.cinemastudio.cinemastudioapp.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public class UpdateApiUserRolesRequest {
    @NotEmpty(message = "Roles list cannot be empty")
    private List<String> roles;
}
