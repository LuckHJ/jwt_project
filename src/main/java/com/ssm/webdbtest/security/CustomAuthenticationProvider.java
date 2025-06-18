package com.ssm.webdbtest.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
//验证用户的凭据（通常是用户名和密码），并根据验证结果返回一个认证对象或抛出异常
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService myuserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(
            MyUserDetailsService myuserDetailsService,
            PasswordEncoder passwordEncoder) {
        this.myuserDetailsService = myuserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    //authenticate() 方法：这是核心认证逻辑。
    //从传入的 Authentication 对象中获取用户名和密码。
    //调用 myuserDetailsService.loadUserByUsername(username) 获取用户详细信息。
    //使用 passwordEncoder.matches(password, userDetails.getPassword()) 检查密码是否匹配。
    //如果匹配成功，创建并返回一个新的 UsernamePasswordAuthenticationToken 对象，包含用户信息和权限。
    //如果不匹配，抛出 BadCredentialsException 异常。
    /*@Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = myuserDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {

            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }*/
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = myuserDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails,  // ✅ 改为传入整个 userDetails 对象
                    password,     // 原始凭证密码（通常不建议保存明文）
                    userDetails.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }
    //supports() 方法：告诉 Spring Security 该 AuthenticationProvider 支持哪种类型的认证令牌。
    //在这里，它支持 UsernamePasswordAuthenticationToken 类型的认证。
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
