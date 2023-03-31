package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;

import java.util.List;

public interface ApiUserService {
    List<ApiUserResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    ApiUserResponse getOneById(String userId);

    ApiUserResponse create(RegisterRequest userRequest);

    ApiUserResponse updateRole(String userId, String role);

    String remove(String userId);
}
