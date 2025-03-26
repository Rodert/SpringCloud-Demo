package com.example.gateway.filter;

import com.example.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 * 
 * 这是Spring Cloud Gateway的全局过滤器，用于拦截所有经过网关的请求，
 * 进行JWT令牌验证。验证通过的请求会被转发到目标微服务，未通过验证的
 * 请求会被拒绝并返回401未授权状态码。
 * 
 * 这个过滤器实现了网关层面的统一认证，使得各个微服务不需要单独实现
 * 认证逻辑，提高了安全性和代码复用率。
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    /**
     * JWT工具类，用于验证令牌
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 白名单路径列表
     * 这些路径不需要JWT令牌验证，可以直接访问
     * 通常包括登录接口、注册接口、公开资源等
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/login",  // 登录接口
            "/actuator/**"  // Spring Boot Actuator端点，用于监控和管理
    );

    /**
     * 过滤器主方法
     * 
     * 处理所有经过网关的HTTP请求，执行JWT令牌验证
     * 
     * 流程:
     * 1. 检查请求路径是否在白名单中，如果是则直接放行
     * 2. 从请求头中获取JWT令牌
     * 3. 验证令牌的有效性
     * 4. 如果令牌有效，将用户信息传递给下游服务
     * 5. 如果令牌无效或不存在，返回401未授权状态码
     * 
     * @param exchange 包含请求和响应信息的Web交换对象
     * @param chain 过滤器链，用于调用下一个过滤器
     * @return 处理结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查路径是否在白名单中，如果是则直接放行
        if (isWhitePath(path)) {
            return chain.filter(exchange);
        }

        // 从请求头中获取JWT令牌
        String token = getToken(request);
        if (token == null) {
            return unauthorized(exchange);  // 如果令牌不存在，返回401
        }

        try {
            // 验证JWT令牌
            String username = jwtUtil.extractUsername(token);
            if (username == null || jwtUtil.isTokenExpired(token)) {
                return unauthorized(exchange);  // 如果令牌无效或已过期，返回401
            }

            // 将用户信息传递到下游服务
            // 通过添加自定义请求头，下游服务可以获取到用户信息
            ServerHttpRequest mutableReq = request.mutate()
                    .header("X-User-Name", username)  // 将用户名添加到请求头
                    .build();
            ServerWebExchange mutableExchange = exchange.mutate()
                    .request(mutableReq)
                    .build();

            // 继续执行过滤器链
            return chain.filter(mutableExchange);
        } catch (Exception e) {
            // 发生异常时返回401
            return unauthorized(exchange);
        }
    }

    /**
     * 检查请求路径是否在白名单中
     * 
     * 支持通配符匹配，如 /actuator/**
     * 
     * @param path 请求路径
     * @return 如果在白名单中则返回true，否则返回false
     */
    private boolean isWhitePath(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> 
                path.matches(pattern.replace("**", ".*")));
    }

    /**
     * 从请求头中获取JWT令牌
     * 
     * 从Authorization请求头中提取Bearer令牌
     * 
     * @param request HTTP请求
     * @return JWT令牌字符串，如果不存在则返回null
     */
    private String getToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // 去掉"Bearer "前缀，提取令牌
        }
        return null;
    }

    /**
     * 设置未授权响应
     * 
     * 当令牌验证失败时，返回401 Unauthorized状态码
     * 
     * @param exchange Web交换对象，包含请求和响应信息
     * @return 完成的响应
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);  // 设置401状态码
        return response.setComplete();  // 完成响应
    }

    /**
     * 获取过滤器执行顺序
     * 
     * 返回一个整数值，数值越小优先级越高
     * -100表示该过滤器应该在大多数其他过滤器之前执行
     * 
     * @return 过滤器顺序值
     */
    @Override
    public int getOrder() {
        return -100; // 确保在其他过滤器之前执行
    }
} 