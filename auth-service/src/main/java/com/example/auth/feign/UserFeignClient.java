package com.example.auth.feign;

import com.example.auth.entity.User;
import com.example.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户服务Feign客户端
 * 
 * 定义调用用户服务的接口方法
 * name属性指定要调用的服务名称，必须与用户服务在注册中心注册的名称一致
 */
@FeignClient(name = "user-service")
public interface UserFeignClient {

    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/users/username/{username}")
    Result<User> getUserByUsername(@PathVariable("username") String username);

    /**
     * 保存或更新用户信息
     * 
     * @param user 用户信息
     * @return 保存后的用户信息
     */
    @PostMapping("/users")
    Result<User> saveUser(@RequestBody User user);

    /**
     * 验证用户凭证
     * 
     * @param credentials 包含用户名和密码的Map
     * @return 验证结果
     */
    @PostMapping("/users/validate")
    Result<Boolean> validateCredentials(@RequestBody Map<String, String> credentials);
} 