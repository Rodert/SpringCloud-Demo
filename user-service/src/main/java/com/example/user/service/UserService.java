package com.example.user.service;

import com.example.user.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 * 
 * 定义用户相关的业务方法
 */
public interface UserService {

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    List<User> findAll();
    
    /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 可选的用户对象
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 找到的用户，如果不存在则返回null
     */
    User findByUsername(String username);
    
    /**
     * 搜索用户
     * 
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     */
    List<User> search(String keyword);
    
    /**
     * 保存或更新用户
     * 
     * @param user 用户信息
     * @return 保存后的用户
     */
    User save(User user);
    
    /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);
    
    /**
     * 验证用户凭证
     * 
     * @param username 用户名
     * @param password 密码
     * @return 验证是否成功
     */
    boolean validateCredentials(String username, String password);
} 