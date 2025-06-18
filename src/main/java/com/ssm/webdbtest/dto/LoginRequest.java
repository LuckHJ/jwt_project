package com.ssm.webdbtest.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    // 自动生成的Getter和Setter方法
    private String username;
    private String password;
}
