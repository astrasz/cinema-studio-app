package com.cinemastudio.cinemastudioapp.security;

import com.cinemastudio.cinemastudioapp.dto.request.AuthRequest;
import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.AuthResponse;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.repository.ApiUserRepository;
import com.cinemastudio.cinemastudioapp.util.RoleName;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final ApiUserRepository apiUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(ApiUserRepository apiUserRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.apiUserRepository = apiUserRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest registerRequest) {

        checkRegisterRequest(registerRequest);

        ApiUser user = new ApiUser();
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(RoleName.USER);

        apiUserRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", RoleName.USER);
        return AuthResponse.builder().token(jwtService.generateJwt(claims, user)).build();
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        ApiUser user = apiUserRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "email", authRequest.getEmail()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        return AuthResponse.builder().token(jwtService.generateJwt(user)).build();
    }

    private void checkRegisterRequest(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getRepeatedPassword())) {
            throw new InvalidRequestParameterException("password, repeatedPassword", String.format("%s %s", registerRequest.getPassword(), registerRequest.getRepeatedPassword()));
        }
        if (apiUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new InvalidRequestParameterException("email", registerRequest.getEmail());
        }
    }
}
