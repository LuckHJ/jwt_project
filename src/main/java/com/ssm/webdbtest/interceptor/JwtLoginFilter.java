package com.ssm.webdbtest.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.webdbtest.dto.LoginRequest;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    //private static final String LOGIN_PATH = "/api/auth/login";

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    // 注意：这里我们使用 super("/api/auth/login") 或者传入 RequestMatcher
    public JwtLoginFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        super("/api/auth/login");  // 设置登录路径
        this.jwtUtils = jwtUtils;
        this.objectMapper = new ObjectMapper();
        setAuthenticationManager(authenticationManager);  // 必须设置！
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.print(authResult.getPrincipal());
        String username = ((User) authResult.getPrincipal()).getUsername();
        Long userId = ((User) authResult.getPrincipal()).getId();
        String token = jwtUtils.generateToken(userId,username);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Authentication failed\"}");
        response.getWriter().flush();
    }
}
