package com.cinemastudio.cinemastudioapp.security;


import com.cinemastudio.cinemastudioapp.model.Token;
import com.cinemastudio.cinemastudioapp.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Autowired
    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }
        token = authHeader.substring(7);
        Token currentToken = tokenRepository.findByToken(token).orElse(null);
        if (currentToken != null) {
            currentToken.setExpired(true);
            currentToken.setInvalidated(true);
            tokenRepository.save(currentToken);
            SecurityContextHolder.clearContext();
        }
    }
}
