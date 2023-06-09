package com.cinemastudio.cinemastudioapp.security;

import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRET = "635266556A586E3272357538782F413F4428472B4B6250645367566B59703373";

    @Autowired
    private Environment env;

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmail(token);
        return email.equals(userDetails.getUsername()) && !isExpired(token);
    }


    public String generateJwt(UserDetails userDetails) {
        return generateJwt(new HashMap<>(), userDetails);
    }

    public String generateJwt(Map<String, Object> claims, UserDetails userDetails) {
        int expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("application.security.jwtExpiration")));
        return createToken(claims, userDetails, expiration);
    }


    public String getEmail(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        int expiration = Integer.parseInt(Objects.requireNonNull(env.getProperty("application.security.refreshJwtExpiration")));
        return createToken(new HashMap<>(), userDetails, expiration);
    }


    private String createToken(Map<String, Object> claims, UserDetails userDetails, int expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Objects.requireNonNull(env.getProperty("application.security.jwtSecret")));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }


}
