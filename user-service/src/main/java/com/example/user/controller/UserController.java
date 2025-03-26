package com.example.user.controller;

import com.example.common.entity.Result;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务控制器
 * 
 * 提供用户相关的REST API，包括查询所有用户、根据ID查询用户、以及搜索用户。
 * 为了简化示例，此控制器使用内存列表模拟数据库存储。
 * 在实际应用中，应该使用数据库如MySQL、MongoDB等进行数据持久化。
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * 用户服务，处理用户相关业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * 模拟用户数据库
     * 使用静态列表存储用户数据，仅用于演示
     */
    private static final List<User> userList = new ArrayList<>();

    /**
     * 静态初始化块
     * 在类加载时初始化测试数据
     */
    static {
        userList.add(new User(1L, "user1", "user1@example.com"));
        userList.add(new User(2L, "user2", "user2@example.com"));
        userList.add(new User(3L, "user3", "user3@example.com"));
    }

    /**
     * 获取所有用户
     * 
     * HTTP GET /users
     * 
     * @return 包含所有用户的响应结果
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.findAll());
    }

    /**
     * 根据ID获取指定用户
     * 
     * HTTP GET /users/{id}
     * 
     * @param id 用户ID
     * @return 如果找到用户则返回用户信息，否则返回错误响应
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(Result::success)
                .orElse(Result.fail("用户不存在"));
    }

    /**
     * 根据用户名获取用户
     * 
     * HTTP GET /users/username/{username}
     * 
     * @param username 用户名
     * @return 如果找到用户则返回用户信息，否则返回错误响应
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return user != null ? Result.success(user) : Result.fail("用户不存在");
    }

    /**
     * 搜索用户
     * 根据关键字搜索用户名或电子邮件中包含该关键字的用户
     * 
     * HTTP GET /users/search?keyword=xxx
     * 
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> filteredUsers = userService.search(keyword);
        return Result.success(filteredUsers);
    }

    /**
     * 创建或更新用户
     * 
     * HTTP POST /users
     * 
     * @param user 用户信息
     * @return 创建或更新后的用户信息
     */
    @PostMapping
    public Result<User> saveUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return Result.success(savedUser);
    }

    /**
     * 验证用户凭证
     * 
     * HTTP POST /users/validate
     * 
     * @param credentials 用户凭证，包含用户名和密码
     * @return 验证结果
     */
    @PostMapping("/validate")
    public Result<Boolean> validateCredentials(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        boolean isValid = userService.validateCredentials(username, password);
        return Result.success(isValid);
    }

    /**
     * 删除用户
     * 
     * HTTP DELETE /users/{id}
     * 
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? Result.success() : Result.fail("删除失败，用户不存在");
    }
} 