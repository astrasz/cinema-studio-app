package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.AddApiUserRequest;
import com.cinemastudio.cinemastudioapp.dto.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.service.ApiUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements ApiUserController {

    private final ApiUserService apiUserService;

    public UserController(ApiUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    @Override
    public ResponseEntity<List<ApiUserResponse>> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<ApiUserResponse> getOneById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ApiUserResponse> update(String id, AddApiUserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ApiUserResponse> create(AddApiUserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<String> remove(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ApiUserResponse> updateRoles(String userId, List<String> roles) {
        return null;
    }
}
