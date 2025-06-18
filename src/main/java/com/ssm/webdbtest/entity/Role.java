package com.ssm.webdbtest.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {

    @TableId
    private Long id;

    private String name; // 角色名称，例如 "ROLE_ADMIN", "ROLE_USER"
}
