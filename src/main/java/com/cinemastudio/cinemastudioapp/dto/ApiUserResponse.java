package com.cinemastudio.cinemastudioapp.dto;


import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ApiUserResponse {

    private String id;

    private String name;
    private String username;
    private String password;
    private List<ApiRoleDto> roles = new ArrayList<>();
}
