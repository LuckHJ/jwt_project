package com.ssm.webdbtest.interceptor;

import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.security.MyUserDetailsService;
import com.ssm.webdbtest.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//@Component: 将该类注册为 Spring 容器管理的 Bean。
//@RequiredArgsConstructor: Lombok 提供的注解，自动生成一个包含所有 final 字段的构造函数（用于依赖注入）。
//extends OncePerRequestFilter: Spring 提供的基类，确保每个请求只被过滤一次（线程安全）。
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    //jwtUtils: 用于解析和验证 JWT Token。
    //userDetailsService: 自定义的用户详情服务，用来根据用户名加载用户信息（如权限、密码等）。
    private final JwtUtils jwtUtils;
    private final MyUserDetailsService userDetailsService;
    //这个方法会在每次 HTTP 请求时执行一次，是实际处理逻辑的地方。
    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //获取请求头中的 Token
        String tokenHeader = request.getHeader("Authorization");
        //判断是否存在并以 "Bearer " 开头
        //如果存在且以 "Bearer " 开头，则截取后面的 Token 字符串。
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            if (jwtUtils.isBlacklisted(token)) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }
            String username = jwtUtils.extractUsername(token);
            //如果用户名非空，且当前线程未认证过（即没有 Authentication），则继续校验 Token。
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //通过 MyUserDetailsService 根据用户名加载完整的用户信息（包括权限、密码等）。
                User userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtils.validateToken(token, userDetails)) {
                    //构造一个认证成功的 Authentication 对象（类型为 UsernamePasswordAuthenticationToken）
                    //并将其放入 SecurityContextHolder，表示当前用户已认证成功
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
