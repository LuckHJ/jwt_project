package com.ssm.webdbtest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssm.webdbtest.mapper.UserMapper;
import com.ssm.webdbtest.entity.User;
import com.ssm.webdbtest.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
//在执行更新、删除操作的方法上加上 @CacheEvict 注解，可以指定在方法执行后清除缓存。
//如果你希望在更新数据库的同时更新缓存，可以使用 @CachePut 注解。
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public User createUser(User user) {
        try {
            logger.info("Attempting to save user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            boolean saved = save(user);
            if (!saved) {
                logger.error("Failed to save user: {}", user.getUsername());
            } else {
                logger.info("Successfully saved user: {}", user.getUsername());
            }
        } catch (Exception e) {
            logger.error("Error occurred while saving user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to create user due to an unexpected error.", e);
        }
        return user;
    }
    @Cacheable(value = "allUsers")
    public List<User> getAllUsers() {
        System.out.println("--------from database---------");//首次调用时显示
        return list(); // 使用 MyBatis-Plus 的 list 方法获取所有记录
    }
    @Cacheable(value = "user")
    public User getUserById(Long id) {
        return getById(id); // 使用 MyBatis-Plus 的 getById 方法根据 ID 获取记录
    }
    @CachePut(value = "user", key = "#user.id")
    @Transactional
    public void updateUser(User user) {
        updateById(user);
    }
    @CacheEvict(value = "allUsers", allEntries = true)
    @Transactional
    public void deleteUser(Long id) {
        removeById(id); // 使用 MyBatis-Plus 的 removeById 方法删除实体
    }
    public void register(String username, String password, String email) {
        if (baseMapper.selectByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAdmin(false); // 默认不是管理员
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        logger.info("baseMapper 类型: {}", baseMapper.getClass());
        save(user);
    }
}
//MyBatis Plus 的 ServiceImpl<M extends BaseMapper<T>, T> 并没有提供 insert(T entity) 这个方法。它提供的是：
//save(T entity) → 插入一个实体（对应数据库 insert）
//getById(Serializable id) → 查询一个实体
//updateById(T entity) → 更新一个实体
//removeById(Serializable id) → 删除一个实体
