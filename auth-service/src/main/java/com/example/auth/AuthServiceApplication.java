package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证服务应用程序入口
 * 
 * @EnableDiscoveryClient 启用服务发现客户端，用于向Nacos注册
 * @EnableFeignClients 启用Feign客户端，支持声明式服务调用
 * @ComponentScan 指定组件扫描的包，确保公共组件被加载
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan({"com.example.auth", "com.example.common"})
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
} 