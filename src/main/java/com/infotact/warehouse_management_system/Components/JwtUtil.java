package com.infotact.warehouse_management_system.Components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // MUST be 32+ chars
    private final String SECRET = "my-secret-key-my-secret-key-123456";

    // Convert to Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate Token
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey()) // FIX
                .compact();
    }

    // Extract Username
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()) // FIX
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
