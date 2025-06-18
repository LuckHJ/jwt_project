package com.ssm.webdbtest.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.webdbtest.dto.LoginRequest;
import com.ssm.webdbtest.dto.LoginResponse;
import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

@Component
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = Logger.getLogger(JwtLoginFilter.class.getName());

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public JwtLoginFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        super("/api/auth/login");
        this.jwtUtils = jwtUtils;
        this.objectMapper = new ObjectMapper();
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                throw new IllegalArgumentException("Username or password is missing");
            }

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            logger.severe("Failed to parse login request: " + e.getMessage());
            throw new RuntimeException("Invalid login request format", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        Long userId = user.getId();

        // 生成 JWT 并缓存到 Redis
        String token = jwtUtils.generateToken(userId, username);
        logger.info("Generated token for user: " + username);

        // 构建统一响应对象
        LoginResponse loginResponse = new LoginResponse(token, username, userId);

        // 设置响应头和内容
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.warning("Authentication failed: " + failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Authentication failed\",\"message\":\"" + failed.getMessage() + "\"}");
        response.getWriter().flush();
    }
}
