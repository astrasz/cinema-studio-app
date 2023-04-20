package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.request.AuthRequest;
import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.dto.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApiAuthController {

    ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest userRequest);

    ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest);

}
