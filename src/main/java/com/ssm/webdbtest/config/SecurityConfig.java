package com.ssm.webdbtest.config;

import com.ssm.webdbtest.CustomAntPathRequestMatcher;
import com.ssm.webdbtest.interceptor.JwtRequestFilter;
import com.ssm.webdbtest.security.CustomAuthenticationProvider;
import com.ssm.webdbtest.interceptor.JwtLoginFilter;
import com.ssm.webdbtest.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestMatcher loginRequestMatcher() {
        return new CustomAntPathRequestMatcher("/api/auth/login");
    }

    @Bean
    public RequestMatcher registerRequestMatcher() {
        return new CustomAntPathRequestMatcher("/api/auth/register");
    }

    @Bean
    public RequestMatcher logoutRequestMatcher() {
        return new CustomAntPathRequestMatcher("/api/auth/logout");
    }
    //创建了一个 JwtLoginFilter 实例，它会在 /api/auth/login 路径上处理用户的登录请求。
    @Bean
    public JwtLoginFilter jwtLoginFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        return new JwtLoginFilter(jwtUtils, authenticationManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtLoginFilter jwtLoginFilter,
            JwtRequestFilter jwtRequestFilter,
            RequestMatcher loginRequestMatcher,
            CustomAuthenticationProvider customAuthProvider) throws Exception {
        // 禁用 CSRF（如果你的应用是无状态的，例如 REST API）
        http.csrf(csrf -> csrf.disable())
                //设置了 Session 管理策略为 STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //允许所有人访问 /api/auth/login, /api/auth/register, 和 /api/auth/logout。
                //所有其他请求都需要身份验证。
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(loginRequestMatcher, registerRequestMatcher(), logoutRequestMatcher())
                        .permitAll()
                        .anyRequest().authenticated())
                // 将 JwtRequestFilter 添加到过滤器链中，在 UsernamePasswordAuthenticationFilter 之前执行。
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                //将 JwtLoginFilter 替换 UsernamePasswordAuthenticationFilter 在过滤器链中的位置。
                //这意味着对于登录请求，将使用 JwtLoginFilter 来进行处理。
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    //创建了一个 ProviderManager 实例，并将其设置为不擦除凭证
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthProvider) throws Exception {
        ProviderManager providerManager = new ProviderManager(customAuthProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
