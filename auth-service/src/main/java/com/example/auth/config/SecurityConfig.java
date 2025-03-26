/*
 * @Author: JavaPub
 * @Date: 2025-03-25 17:17:42
 * @LastEditors: your name
 * @LastEditTime: 2025-03-26 13:43:45
 * @Description: Here is the JavaPub code base. Search JavaPub on the whole web.
 * @FilePath: /SpringCloud-Demo/auth-service/src/main/java/com/example/auth/config/SecurityConfig.java
 */
package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 * <p>
 * 用于配置认证服务的安全相关设置，包括：
 * 1. 用户认证方式（从数据库或远程服务获取用户信息）
 * 2. 密码加密方式
 * 3. 请求授权规则
 * 4. 认证管理器
 */
@Configuration  // 标记为Spring配置类
@EnableWebSecurity  // 启用Spring Security的Web安全支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 通过构造函数注入UserDetailsService，避免循环依赖
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 配置认证管理器
     * <p>
     * 定义如何验证用户身份
     * 使用自定义UserDetailsService加载用户信息
     *
     * @param auth 认证管理器构建器
     * @throws Exception 如果配置过程中发生错误
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义UserDetailsService和密码编码器
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
            
        // 保留注释掉的内存用户配置，作为备用方案
        /*
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER");
        */
    }

    /**
     * 配置HTTP安全策略
     * <p>
     * 定义哪些URL路径应该被保护，哪些可以公开访问
     *
     * @param http HTTP安全构建器
     * @throws Exception 如果配置过程中发生错误
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 关闭CSRF保护，简化API调用
                .authorizeRequests()  // 开始定义请求授权规则
                .antMatchers("/auth/login", "/auth/register").permitAll()  // 登录和注册接口允许所有人访问
                .anyRequest().authenticated();  // 其他所有请求都需要认证
    }

    /**
     * 暴露认证管理器Bean
     * <p>
     * 认证管理器用于处理认证请求
     * 在Controller中需要使用它来验证用户身份
     *
     * @return 认证管理器实例
     * @throws Exception 如果获取认证管理器过程中发生错误
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
} 