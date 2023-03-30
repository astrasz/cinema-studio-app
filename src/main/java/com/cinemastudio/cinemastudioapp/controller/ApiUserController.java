package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.AddApiUserRequest;
import com.cinemastudio.cinemastudioapp.dto.ApiUserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApiUserController extends ApiController<AddApiUserRequest, ApiUserResponse> {
    ResponseEntity<ApiUserResponse> updateRoles(String userId, List<String> roles);
}
