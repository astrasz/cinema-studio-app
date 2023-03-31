package com.cinemastudio.cinemastudioapp.controller.impl;

import com.cinemastudio.cinemastudioapp.controller.ApiAuthController;
import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.dto.request.AuthRequest;
import com.cinemastudio.cinemastudioapp.dto.response.AuthResponse;
import com.cinemastudio.cinemastudioapp.security.AuthService;
import com.cinemastudio.cinemastudioapp.service.ApiUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/account")
@RestController
public class AuthController implements ApiAuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok().body(authService.authenticate(authRequest));

    }
}
