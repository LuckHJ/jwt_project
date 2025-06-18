package com.ssm.webdbtest.service;

import com.ssm.webdbtest.entity.User;

import java.util.List;

public interface UserService{
    User createUser(User user);
    List<User> getAllUsers();// 使用 MyBatis-Plus 的 list 方法获取所有记录
    User getUserById(Long id);// 使用 MyBatis-Plus 的 getById 方法根据 ID 获取记录
    void updateUser(User user);
    void deleteUser(Long id);// 使用 MyBatis-Plus 的 removeById 方法删除实体
    void register(String username, String password, String email);
}
