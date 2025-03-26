package com.example.auth.service.impl;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类 - 数据库方式
 * 
 * 通过JPA Repository访问数据库中的用户信息
 * 实际项目中会有更复杂的业务逻辑和缓存策略
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public User save(User user) {
        // 如果是新用户或密码已修改，需要对密码进行加密
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().startsWith("$2a$"))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }
        
        return passwordEncoder.matches(password, user.getPassword());
    }
} 