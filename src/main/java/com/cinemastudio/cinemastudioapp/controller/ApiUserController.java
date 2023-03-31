package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.request.ApiUserUpdateRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ApiUserController {

    ResponseEntity<List<ApiUserResponse>> getAllUsers(Integer pageNr, Integer number, String sortBy, String sortDir);

    ResponseEntity<ApiUserResponse> getUserById(@PathVariable String id);

    public ResponseEntity<ApiUserResponse> updateUser(@PathVariable String id, @RequestBody ApiUserUpdateRequest request);

    public ResponseEntity<String> removeUser(@PathVariable String id);

    public ResponseEntity<ApiUserResponse> updateUserRole(String userId, String role);
}
