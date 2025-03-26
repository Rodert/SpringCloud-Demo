package com.example.auth.service;

import com.example.auth.entity.User;

/**
 * 用户服务接口
 * 
 * 定义访问用户数据的方法，可以通过实现类连接不同的数据源
 * 例如：数据库、远程服务调用、缓存等
 */
public interface UserService {
    
    /**
     * 通过用户名查找用户
     * 
     * @param username 用户名
     * @return 找到的用户，如果不存在则返回null
     */
    User findByUsername(String username);
    
    /**
     * 保存或更新用户信息
     * 
     * @param user 用户信息
     * @return 保存后的用户信息
     */
    User save(User user);
    
    /**
     * 验证用户凭证
     * 
     * @param username 用户名
     * @param password 明文密码
     * @return 如果验证成功则返回true，否则返回false
     */
    boolean validateCredentials(String username, String password);
} 