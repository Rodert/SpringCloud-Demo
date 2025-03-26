package com.example.auth.service.impl;

import com.example.auth.entity.User;
import com.example.auth.feign.UserFeignClient;
import com.example.auth.service.UserService;
import com.example.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Primary;
// import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

/**
 * 用户服务实现类 - 远程服务调用方式
 * 
 * 通过Feign调用用户服务获取用户信息
 * 这种方式适合于微服务架构，认证服务和用户数据分离的情况
 * 
 * 使用@Primary注解使其成为首选的实现类
 */
// @Service
// @Primary
@Qualifier("remoteUserService")
public class RemoteUserServiceImpl implements UserService {

    private final UserFeignClient userFeignClient;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public RemoteUserServiceImpl(UserFeignClient userFeignClient, PasswordEncoder passwordEncoder) {
        this.userFeignClient = userFeignClient;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User findByUsername(String username) {
        Result<User> result = userFeignClient.getUserByUsername(username);
        if (result.getCode() == 200 && result.getData() != null) {
            return result.getData();
        }
        return null;
    }
    
    @Override
    public User save(User user) {
        // 如果是新用户或密码已修改，需要对密码进行加密
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().startsWith("$2a$"))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        Result<User> result = userFeignClient.saveUser(user);
        if (result.getCode() == 200 && result.getData() != null) {
            return result.getData();
        }
        
        throw new RuntimeException("Failed to save user: " + result.getMessage());
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        // 两种方式：
        // 1. 获取用户信息后在本地验证密码
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
        
        // 2. 通过远程服务验证密码（取消注释使用）
        /*
        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );
        Result<Boolean> result = userFeignClient.validateCredentials(credentials);
        return result.getCode() == 200 && Boolean.TRUE.equals(result.getData());
        */
    }
} 