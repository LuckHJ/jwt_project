package com.ssm.webdbtest.utils;

import com.ssm.webdbtest.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
public class JwtUtils {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration}")
    private long EXPIRATION; // 单位：毫秒

    public String generateToken(Long userId, String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();

        // 保存 token 到 Redis，并设置过期时间
        String userTokenKey = "userTokenById:" + userId;
        redisTemplate.opsForValue().set(userTokenKey, token, EXPIRATION, TimeUnit.MILLISECONDS);

        return token;
    }

    // 校验 token 是否是该用户当前有效的 token
    public boolean validateToken(String token, User user) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            String username = extractUsername(token);
            String currentToken = redisTemplate.opsForValue().get("userTokenById:" + user.getId());

            return username.equals(user.getUsername())
                    && !isTokenExpired(token)
                    && token.equals(currentToken); // 确保是当前有效的 token
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 提取用户名
    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    // 判断是否过期
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
    /**
     * 获取 token 的剩余有效毫秒数
     */
    private long getExpiration(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expirationDate.getTime() - System.currentTimeMillis();
    }
    /**
     * 将 token 加入黑名单
     */
    public void addToBlacklist(String token) {
        // 获取当前 token 的剩余过期时间
        long expiration = getExpiration(token);

        // 存入 Redis，key 形式为 blacklist:{token}
        String key = "blacklist:" + token;
        redisTemplate.opsForValue().set(key, "invalid", expiration, TimeUnit.MILLISECONDS);
    }
    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
