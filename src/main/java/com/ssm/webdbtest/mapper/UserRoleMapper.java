package com.ssm.webdbtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssm.webdbtest.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    // 可以在此处添加自定义查询方法
}
