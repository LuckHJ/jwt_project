package com.ssm.webdbtest.config;

import com.ssm.webdbtest.CustomAntPathRequestMatcher;
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
    public JwtLoginFilter jwtLoginFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        return new JwtLoginFilter(jwtUtils, authenticationManager);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtLoginFilter jwtLoginFilter,
            RequestMatcher loginRequestMatcher,
            CustomAuthenticationProvider customAuthProvider) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(loginRequestMatcher, registerRequestMatcher()) // 同时放行登录和注册
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthProvider) throws Exception {
        ProviderManager providerManager = new ProviderManager(customAuthProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
