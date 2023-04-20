package com.cinemastudio.cinemastudioapp.dto.response;


import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ApiUserResponse {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
}
