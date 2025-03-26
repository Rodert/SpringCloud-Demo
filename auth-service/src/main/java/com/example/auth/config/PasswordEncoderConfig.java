package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置类
 * 
 * 将PasswordEncoder的配置从SecurityConfig中分离出来，
 * 以避免循环依赖问题。
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * 配置密码编码器
     * 
     * 用于对用户密码进行加密和验证
     * 使用BCrypt算法，这是一种强哈希算法，专为密码存储设计
     *
     * @return BCrypt密码编码器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 