package com.example.order.controller;

import com.example.common.entity.Result;
import com.example.order.entity.Order;
import com.example.order.entity.User;
import com.example.order.feign.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务控制器
 * 
 * 提供订单相关的REST API，包括查询所有订单、根据ID查询订单、查询用户订单、创建订单等功能。
 * 该控制器展示了微服务间通信的示例：订单服务通过Feign客户端调用用户服务获取用户信息。
 * 为了简化示例，此控制器使用内存列表模拟数据库存储。
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * 用户服务Feign客户端
     * 用于调用用户服务的接口，获取用户信息
     */
    @Autowired
    private UserClient userClient;

    /**
     * 模拟订单数据库
     * 使用静态列表存储订单数据，仅用于演示
     */
    private static final List<Order> orderList = new ArrayList<>();

    /**
     * 静态初始化块
     * 在类加载时初始化测试数据
     */
    static {
        orderList.add(new Order(1L, 1L, "iPhone 13", 1, new BigDecimal("6999"), LocalDateTime.now(), null));
        orderList.add(new Order(2L, 2L, "MacBook Pro", 1, new BigDecimal("12999"), LocalDateTime.now(), null));
        orderList.add(new Order(3L, 1L, "iPad Pro", 1, new BigDecimal("5999"), LocalDateTime.now(), null));
    }

    /**
     * 获取所有订单
     * 
     * HTTP GET /orders
     * 
     * @return 包含所有订单的响应结果
     */
    @GetMapping
    public Result<List<Order>> getAllOrders() {
        return Result.success(orderList);
    }

    /**
     * 根据ID获取订单信息
     * 
     * 此方法演示了微服务间的通信：获取订单后，通过Feign客户端调用用户服务
     * 获取用户信息，并将用户信息关联到订单对象中。
     * 
     * HTTP GET /orders/{id}
     * 
     * @param id 订单ID
     * @return 如果找到订单则返回订单信息（包含用户信息），否则返回错误响应
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        return orderList.stream()
                .filter(order -> order.getId().equals(id))  // 根据ID过滤订单
                .findFirst()                                // 获取第一个匹配的订单
                .map(order -> {
                    // 调用用户服务获取用户信息
                    Result<User> userResult = userClient.getUserById(order.getUserId());
                    // 如果成功获取用户信息，则关联到订单对象
                    if (userResult.getCode() == 200 && userResult.getData() != null) {
                        order.setUser(userResult.getData());
                    }
                    // 返回包含用户信息的订单
                    return Result.success(order);
                })
                .orElse(Result.fail("订单不存在"));         // 如果未找到订单，返回错误响应
    }

    /**
     * 获取指定用户的所有订单
     * 
     * 首先根据用户ID筛选订单，然后通过Feign客户端调用用户服务
     * 获取用户信息，并将用户信息关联到每个订单对象。
     * 
     * HTTP GET /orders/user/{userId}
     * 
     * @param userId 用户ID
     * @return 指定用户的所有订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        // 根据用户ID筛选订单
        List<Order> userOrders = orderList.stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
        
        // 调用用户服务获取用户信息
        Result<User> userResult = userClient.getUserById(userId);
        if (userResult.getCode() == 200 && userResult.getData() != null) {
            User user = userResult.getData();
            // 将用户信息关联到每个订单对象
            userOrders.forEach(order -> order.setUser(user));
        }
        
        // 返回用户的订单列表
        return Result.success(userOrders);
    }

    /**
     * 创建新订单
     * 
     * HTTP POST /orders
     * 
     * @param order 订单信息（请求体中的JSON将被反序列化为Order对象）
     * @return 创建成功的订单信息
     */
    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order) {
        // 简单模拟创建订单，生成ID和创建时间
        order.setId((long) (orderList.size() + 1));
        order.setCreateTime(LocalDateTime.now());
        orderList.add(order);
        return Result.success(order);
    }
} 