package com.example.auth.entity;

/**
 * 登录请求实体类
 * 
 * 用于接收客户端发送的登录请求数据，包含用户名和密码信息。
 * 在登录过程中，前端将用户输入的登录信息封装为JSON对象发送到服务器，
 * 服务器将其反序列化为此类的实例。
 */
public class LoginRequest {
    /**
     * 用户名
     * 用于用户身份识别
     */
    private String username;
    
    /**
     * 密码
     * 用于验证用户身份
     */
    private String password;
    
    /**
     * 无参构造函数
     * Spring MVC在反序列化JSON数据时需要使用
     */
    public LoginRequest() {
    }
    
    /**
     * 带参数的构造函数
     * 
     * @param username 用户名
     * @param password 密码
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置用户名
     * 
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取密码
     * 
     * @return 密码
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置密码
     * 
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
} 