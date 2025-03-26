package com.example.order.entity;

/**
 * 用户实体类
 * 
 * 包含用户的基本信息：ID、用户名、密码、邮箱
 * 主要用于订单服务中关联用户信息
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    
    public User() {
    }
    
    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    public User(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
} 