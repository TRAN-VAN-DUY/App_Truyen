package com.apptruyen.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    // Dùng SLF4J trực tiếp thay vì @Slf4j để tránh vấn đề Lombok annotation
    // processor
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    /**
     * Lấy userId từ JWT token trong header Authorization.
     * JWT được tạo bởi team auth, subject = userId (Integer).
     */
    public Integer getUserIdFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        Claims claims = parseClaims(token);
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * Đọc claim "role" từ JWT token.
     * Fallback về "ROLE_USER" nếu không có.
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            Object roleObj = claims.get("role");
            String role = roleObj != null ? roleObj.toString() : "ROLE_USER";
            log.debug("Extracted role from token: {}", role);
            return role;
        } catch (Exception e) {
            log.warn("Failed to extract role from token: {}", e.getMessage());
            return "ROLE_USER";
        }
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT invalid: {}", e.getMessage());
            return false;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtException("Missing or invalid Authorization header");
        }
        return header.substring(7);
    }

    private Claims parseClaims(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
