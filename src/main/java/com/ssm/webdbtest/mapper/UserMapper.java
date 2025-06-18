package com.ssm.webdbtest.mapper;

import com.ssm.webdbtest.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如果需要自定义 SQL 操作，可以在这里定义方法
    @Select("Select password from user where id= #{id}")
    String getPassword(Long id);
    @Select("Select * from user where username= #{userName}")
    User selectByUsername(String userName);
    //密码必须使用 BCryptPasswordEncoder 加密后再存储
    //INSERT INTO user (username, password, roles) VALUES (
    //    'admin',
    //    '$2a$10$YzZVZlNkZTEyMjM0NTY3OE9iamVjdC5rZXk=', -- BCrypt 加密后的密码
    //    'ROLE_ADMIN'
    //);
}
