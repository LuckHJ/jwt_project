package com.ssm.webdbtest.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Collection;

@Data
@TableName("user") // 指定对应的数据库表名
public class User implements UserDetails {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String username;
    private String password;

    @TableField("is_admin")
    private boolean isAdmin;

    private String email;

    @TableField("account_non_expired")
    private boolean accountNonExpired;

    @TableField("account_non_locked")
    private boolean accountNonLocked;

    @TableField("credentials_non_expired")
    private boolean credentialsNonExpired;

    @TableField("enabled")
    private boolean enabled;
    @TableField(exist = false)
    @Transient // authorities 不应直接映射到数据库字段
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    // 必须保留无参构造函数
    public User() {}

    // 完整构造函数
    public User(Long id, String username, String password, boolean isAdmin, String email,
                boolean accountNonExpired, boolean accountNonLocked,
                boolean credentialsNonExpired, boolean enabled,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.email = email;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
