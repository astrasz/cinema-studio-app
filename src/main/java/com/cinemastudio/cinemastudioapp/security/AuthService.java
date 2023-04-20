package com.cinemastudio.cinemastudioapp.security;

import com.cinemastudio.cinemastudioapp.dto.request.AuthRequest;
import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.AuthResponse;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.model.Token;
import com.cinemastudio.cinemastudioapp.repository.ApiUserRepository;
import com.cinemastudio.cinemastudioapp.repository.TokenRepository;
import com.cinemastudio.cinemastudioapp.util.RoleName;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final ApiUserRepository apiUserRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthService(ApiUserRepository apiUserRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository) {
        this.apiUserRepository = apiUserRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
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
        String token = jwtService.generateJwt(claims, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveApiUserToken(token, user);

        return AuthResponse.builder().token(token).refreshToken(refreshToken).build();
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        ApiUser user = apiUserRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "email", authRequest.getEmail()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        String token = jwtService.generateJwt(claims, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        invalidateUsersTokens(user);
        saveApiUserToken(token, user);

        return AuthResponse.builder().token(token).refreshToken(refreshToken).build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.getEmail(refreshToken);
        if (email != null) {
            ApiUser apiUser = apiUserRepository.findByEmail(email).orElseThrow(() -> new InvalidRequestParameterException("token", refreshToken));
            if (jwtService.isTokenValid(refreshToken, apiUser)) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("role", apiUser.getRole());
                String accessToken = jwtService.generateJwt(claims, apiUser);
                invalidateUsersTokens(apiUser);
                saveApiUserToken(accessToken, apiUser);
                AuthResponse authResponse = AuthResponse.builder().token(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveApiUserToken(String accessToken, ApiUser user) {
//        Token tokenEntity = Token.builder()
//                .token(accessToken)
//                .apiUser(user)
//                .build();
        Token token = new Token();
        token.setToken(accessToken);
        token.setApiUser(user);
        tokenRepository.save(token);
    }

    private void checkRegisterRequest(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getRepeatedPassword())) {
            throw new InvalidRequestParameterException("password, repeatedPassword", String.format("%s %s", registerRequest.getPassword(), registerRequest.getRepeatedPassword()));
        }
        if (apiUserRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new InvalidRequestParameterException("email", registerRequest.getEmail());
        }
    }

    private void invalidateUsersTokens(ApiUser apiUser) {
        List<Token> usersValidTokens = tokenRepository.findAllValidTokensByUserId(apiUser.getId());
        if (usersValidTokens.isEmpty()) {
            return;
        }
        usersValidTokens.forEach(token -> {
            token.setInvalidated(true);
            token.setExpired(true);
        });


        tokenRepository.saveAll(usersValidTokens);

    }
}
