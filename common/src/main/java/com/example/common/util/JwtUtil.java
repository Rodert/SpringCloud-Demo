package com.example.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * JWT工具类
 * 
 * 用于JWT令牌的生成、解析和验证，是微服务认证体系的核心组件。
 * JWT (JSON Web Token) 是一种基于JSON的开放标准（RFC 7519），用于在各方之间安全地传输信息。
 * 本工具类实现了JWT的主要操作，供认证服务、网关和各个微服务使用。
 */
@Component
public class JwtUtil {

    /**
     * JWT签名密钥
     * 通过配置文件注入，用于对令牌进行签名和验证
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 令牌过期时间（秒）
     * 通过配置文件注入，指定令牌的有效期
     */
    @Value("${jwt.expiration:86400}")
    private Long expiration;

    /**
     * 从令牌中提取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从令牌中提取过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从令牌中提取指定的声明
     * 
     * @param token JWT令牌
     * @param claimsResolver 声明解析函数
     * @param <T> 返回类型
     * @return 解析后的声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从令牌中提取所有声明
     * 
     * @param token JWT令牌
     * @return 所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 检查令牌是否已过期
     * 
     * @param token JWT令牌
     * @return 如果过期返回true，否则返回false
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    /**
     * 生成JWT令牌
     * 
     * 使用用户名作为主题，设置签发时间和过期时间，
     * 并使用配置的密钥和HS256算法进行签名
     * 
     * @param username 用户名，作为令牌的主题
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // 设置主题（用户名）
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, secret)  // 使用HS256算法和密钥签名
                .compact();  // 生成JWT
    }
} 