package com.cinemastudio.cinemastudioapp.controller.impl;

import com.cinemastudio.cinemastudioapp.controller.ApiUserController;
import com.cinemastudio.cinemastudioapp.dto.request.ApiUserUpdateRequest;
import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.service.ApiUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements ApiUserController {

    private final ApiUserService apiUserService;

    @Autowired
    public UserController(ApiUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ApiUserResponse>> getAllUsers(Integer pageNr, Integer number, String sortBy, String sortDir) {
        return ResponseEntity.ok().body(apiUserService.getAll(pageNr, number, sortBy, sortDir));
    }

    @Override
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiUserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok().body(apiUserService.getOneById(id));
    }

    @Override
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiUserResponse> updateUser(@PathVariable String id, @Valid @RequestBody ApiUserUpdateRequest request) {
        return null;
    }

    @Override
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable String id) {
        return ResponseEntity.ok().body(apiUserService.remove(id));
    }

    @Override
    @PutMapping("/users/{userId}/{role}")
    public ResponseEntity<ApiUserResponse> updateUserRole(@PathVariable String userId, @PathVariable String role) {
        return ResponseEntity.ok().body(apiUserService.updateRole(userId, role));
    }
}
