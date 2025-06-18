package com.ssm.webdbtest.controller;

import com.ssm.webdbtest.dto.LoginRequest;
import com.ssm.webdbtest.dto.LoginResponse;
import com.ssm.webdbtest.dto.RegisterRequest;
import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.service.UserService;
import com.ssm.webdbtest.service.impl.UserServiceImpl;
import com.ssm.webdbtest.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    //authenticationManager: 用于触发 Spring Security 的认证流程。
    private final AuthenticationManager authenticationManager;
    //JWT 工具类，用于生成 token。
    private final JwtUtils jwtUtils;
    //private final MyUserDetailsService userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String username = ((User) authentication.getPrincipal()).getUsername();
        Long userId = ((User) authentication.getPrincipal()).getId();
        String token = jwtUtils.generateToken(userId,username);
        System.out.print(token);
        return ResponseEntity.ok(new LoginResponse(token,username,userId));
    }
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok("注册成功，请登录");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {
        System.out.print("Logout");
        if (token != null && token.startsWith("Bearer ")) {
            System.out.print(token);
            token = token.substring(7);
            jwtUtils.addToBlacklist(token);
            return ResponseEntity.ok().body("Logout successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
