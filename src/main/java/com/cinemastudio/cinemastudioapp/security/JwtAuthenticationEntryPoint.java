package com.cinemastudio.cinemastudioapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message;

        final Exception exception = (Exception) request.getAttribute("exception");
        final Exception expired = (Exception) request.getAttribute("expired");
        if (expired != null) {
            message = expired.getMessage();
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("expired", message.substring(0, message.indexOf("."))));
            response.getOutputStream().write(body);
        } else if (exception != null) {
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", exception.getMessage()));
            response.getOutputStream().write(body);
        } else {
            if (authException.getCause() != null) {
                message = authException.getCause().toString() + " " + authException.getMessage();
            } else {
                message = authException.getMessage();
            }
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

            response.getOutputStream().write(body);
        }


    }
}
