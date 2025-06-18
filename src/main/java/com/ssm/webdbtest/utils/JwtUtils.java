package com.ssm.webdbtest.utils;

import com.ssm.webdbtest.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION;
    // private final String secretKey;
    // private final long expiration;

    // public JwtUtils(@Value("${app.jwt.secret-key}") String secretKey,
    //                 @Value("${app.jwt.expiration}") long expiration) {
    //     this.secretKey = secretKey;
    //     this.expiration = expiration;
    // }
    // 生成 Token
    public String generateToken(Long userId, String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(userId.toString()) // 使用唯一ID作为 subject
                .claim("username", username)   // 把用户名放进额外的 claims
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }
    // 提取用户名
    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class); // 从 claim 中获取 username
    }

    // 验证 Token 是否有效
    public boolean validateToken(String token, User user) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            String username = extractUsername(token);
            // Also check if token is expired
            if (isTokenExpired(token)) {
                return false;
            }
            return username.equals(user.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 判断 Token 是否过期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // 提取过期时间
    private Date extractExpiration(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
