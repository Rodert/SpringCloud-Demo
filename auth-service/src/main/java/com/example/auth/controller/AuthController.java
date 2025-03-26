package com.example.auth.controller;

import com.example.auth.entity.LoginRequest;
import com.example.common.entity.Result;
import com.example.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * 处理用户认证相关的请求，如登录、获取令牌等。
 * 这是整个系统安全架构的入口点，负责验证用户身份并颁发JWT令牌。
 * 登录成功后，客户端将获得一个JWT令牌，用于后续请求的认证。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Spring Security认证管理器
     * 用于验证用户提供的凭证
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * JWT工具类
     * 用于生成和验证JWT令牌
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录接口
     * 
     * 处理用户登录请求，验证用户凭证并生成JWT令牌
     * 
     * 流程:
     * 1. 接收用户名和密码
     * 2. 通过认证管理器验证凭证
     * 3. 如果验证成功，生成JWT令牌
     * 4. 返回令牌给客户端
     * 
     * @param loginRequest 包含用户名和密码的登录请求体
     * @return 包含JWT令牌的响应结果
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("处理登录请求: " + loginRequest.getUsername());
            
            // 创建认证令牌对象（包含用户名和密码）
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            System.out.println("认证成功，生成JWT令牌");

            // 如果认证成功，将认证信息存储在上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 为用户生成JWT令牌
            String jwt = jwtUtil.generateToken(loginRequest.getUsername());
            
            // 将令牌封装到响应中
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", jwt);
            
            // 返回成功响应，包含JWT令牌
            return Result.success(tokenMap);
        } catch (BadCredentialsException e) {
            System.err.println("认证失败：凭证无效 - " + e.getMessage());
            return Result.fail(401, "用户名或密码错误");
        } catch (AuthenticationException e) {
            System.err.println("认证异常：" + e.getClass().getName() + " - " + e.getMessage());
            return Result.fail(401, "认证失败: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("登录过程中发生异常: " + e.getClass().getName());
            e.printStackTrace();
            return Result.fail(500, "登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        System.err.println("控制器异常: " + e.getClass().getName() + " - " + e.getMessage());
        e.printStackTrace();
        return Result.fail(500, "服务器内部错误: " + e.getMessage());
    }
} 