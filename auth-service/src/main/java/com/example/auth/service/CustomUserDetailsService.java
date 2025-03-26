package com.example.auth.service;

import com.example.auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务
 * 
 * 实现Spring Security的UserDetailsService接口
 * 用于在认证过程中根据用户名加载用户信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    
    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 根据用户名加载用户信息
     * 
     * Spring Security在认证时会调用此方法获取用户信息
     * 然后与用户提供的凭证进行比对
     * 
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        return user;
    }
} 