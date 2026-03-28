package com.phdprogress.phd_progress.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration}") long expiration) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        Claims claims = extractAllClaims(token);
        return username.equals(claims.getSubject()) && claims.getExpiration().after(new Date());
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object roles = extractAllClaims(token).get("roles");
        return roles instanceof List<?> roleList
                ? roleList.stream().map(String::valueOf).toList()
                : List.of();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
