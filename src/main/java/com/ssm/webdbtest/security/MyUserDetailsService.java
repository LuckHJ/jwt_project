package com.ssm.webdbtest.security;

import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
//用户提交用户名和密码到 /login
//Spring Security 拦截请求并创建 UsernamePasswordAuthenticationToken
//调用 AuthenticationManager.authenticate(token)
//AuthenticationManager 找到合适的 AuthenticationProvider
//Provider 调用 UserDetailsService.loadUserByUsername(username)
//得到 UserDetails 后，比较密码是否匹配（使用 PasswordEncoder）
//如果匹配，认证成功，生成已认证的 Authentication 对象
//将其放入 SecurityContext，用户登录成功
//可以访问受保护资源
@Service
@RequiredArgsConstructor
public class MyUserDetailsService {
    private final UserMapper userMapper;
    //private final PasswordEncoder passwordEncoder;
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用userMapper根据用户名查询数据库
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        //创建一个实现了 UserDetails 接口的对象，这个对象包含了用户的详细信息，包括用户名、密码和权限（角色）。
        //Spring Security 要求使用 UserDetails 或其子类
        //可以自定义 User 类
        //只要实现 UserDetails 接口即可
        return user;
    }
}
