package com.example.auth.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;

/**
 * 数据初始化器
 * 
 * 在应用程序启动时，自动创建测试用户
 * 仅用于开发和测试环境
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查数据库中是否已有用户
        if (userRepository.count() == 0) {
            System.out.println("初始化测试用户数据...");
            
            // 创建普通用户
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setEmail("user@example.com");
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            
            // 创建管理员用户
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setRoles(Arrays.asList("ADMIN"));
            userRepository.save(admin);
            
            System.out.println("测试用户数据初始化完成。");
        }
    }
} 