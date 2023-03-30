package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.AddApiUserRequest;
import com.cinemastudio.cinemastudioapp.dto.ApiUserResponse;

import java.util.List;

public interface ApiUserService {
    List<ApiUserResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    ApiUserResponse getOneById(String userId);

    ApiUserResponse create(AddApiUserRequest userRequest);

    ApiUserResponse updateRoles(String userId, List<String> roles);

    String remove(String userId);
}
