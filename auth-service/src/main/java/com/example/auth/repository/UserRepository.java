package com.example.auth.repository;

import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户数据访问接口
 * 
 * 使用Spring Data JPA实现对用户表的CRUD操作
 * JpaRepository提供了基本的数据库操作方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * 
     * Spring Data JPA会根据方法名自动生成SQL查询
     * 
     * @param username 用户名
     * @return 找到的用户，如果不存在则返回null
     */
    User findByUsername(String username);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱地址
     * @return 如果存在则返回true，否则返回false
     */
    boolean existsByEmail(String email);
} 