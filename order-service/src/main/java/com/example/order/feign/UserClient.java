package com.example.order.feign;

import com.example.common.entity.Result;
import com.example.order.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务Feign客户端
 * 
 * Feign是一个声明式的HTTP客户端，使微服务之间的调用更加简单。
 * 通过在接口上添加注解，Feign可以自动生成HTTP请求的实现代码。
 * 
 * 这个接口定义了订单服务需要从用户服务获取的API。
 * 当服务实例化时，Spring Cloud会自动创建这个接口的实现类，
 * 其中包含了发送HTTP请求到用户服务的代码。
 * 
 * @FeignClient注解指定了目标服务的名称，这个名称必须与用户服务
 * 在注册中心(Nacos)中注册的服务名称一致。
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * 根据ID获取用户信息
     * 
     * 这个方法会向用户服务发送GET请求，路径为 /users/{id}
     * 路径中的{id}参数会被方法参数替换
     * 
     * 注意：这个方法的签名必须与用户服务中对应接口的签名保持一致，
     * 包括返回类型、参数类型和参数注解
     * 
     * @param id 用户ID
     * @return 包含用户信息的响应结果
     */
    @GetMapping("/users/{id}")
    Result<User> getUserById(@PathVariable("id") Long id);
} 