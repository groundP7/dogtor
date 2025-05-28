package com.ground.dogtor.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Configuration
public class JwtConfig {

    private final SecretKey key;
    private final long accessTokenValidTime = 60 * 60 * 1000L;      // Access Token 1시간
    private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;  // Refresh Token 7일

    public JwtConfig(@Value("${jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT secret key must not be null or empty");
        }
        try {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT secret key: " + e.getMessage());
        }
    }

    // Access Token 생성
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // JWT 토큰 검증 및 클레임 추출
    public Claims getTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = getTokenClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // Refresh Token 만료 임박 여부 확인 (3일 미만 남은 경우)
    public boolean isRefreshTokenNearExpiration(String token) {
        try {
            Claims claims = getTokenClaims(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            
            // 만료까지 남은 시간이 3일 이하인 경우
            long timeUntilExpiration = expiration.getTime() - now.getTime();
            return timeUntilExpiration <= (3 * 24 * 60 * 60 * 1000L);
        } catch (Exception e) {
            return true;
        }
    }
}
