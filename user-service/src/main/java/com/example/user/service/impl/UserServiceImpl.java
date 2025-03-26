package com.example.user.service.impl;

import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * 实现UserService接口的业务逻辑
 * 为了简化示例，使用内存列表存储用户数据
 * 实际应用中应该使用数据库
 */
@Service
public class UserServiceImpl implements UserService {

    // 用于密码加密和验证
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // 模拟用户数据库
    private static final List<User> userList = new ArrayList<>();
    
    // 静态初始化，添加测试用户
    static {
        // 添加几个测试用户，密码为用户名
        User user1 = new User(1L, "user1", "user1@example.com");
        User user2 = new User(2L, "user2", "user2@example.com");
        User user3 = new User(3L, "user3", "user3@example.com");
        
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public User findByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<User> search(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(userList);
        }
        
        return userList.stream()
                .filter(user -> 
                    user.getUsername().contains(keyword) || 
                    (user.getEmail() != null && user.getEmail().contains(keyword)))
                .collect(Collectors.toList());
    }
    
    @Override
    public User save(User user) {
        // 如果是新用户
        if (user.getId() == null) {
            // 生成新ID
            long newId = userList.isEmpty() ? 1 : userList.stream()
                    .mapToLong(User::getId)
                    .max()
                    .getAsLong() + 1;
            user.setId(newId);
            
            // 如果提供了密码，进行加密
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            userList.add(user);
        } else {
            // 更新现有用户
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId().equals(user.getId())) {
                    // 如果提供了新密码并且不是已加密的密码
                    if (user.getPassword() != null && !user.getPassword().isEmpty() 
                            && !user.getPassword().startsWith("$2a$")) {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                    } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
                        // 保留原密码
                        user.setPassword(userList.get(i).getPassword());
                    }
                    
                    userList.set(i, user);
                    break;
                }
            }
        }
        
        return user;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int initialSize = userList.size();
        userList.removeIf(user -> user.getId().equals(id));
        return userList.size() < initialSize;
    }
    
    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        if (user == null || user.getPassword() == null) {
            return false;
        }
        
        // 如果用户密码还没有加密（模拟数据），加密后保存
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            save(user);
        }
        
        return passwordEncoder.matches(password, user.getPassword());
    }
} 