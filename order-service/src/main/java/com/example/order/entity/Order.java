package com.example.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long userId;
    private String productName;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createTime;
    
    // 非数据库字段，用于存储关联的用户信息
    private User user;
    
    public Order() {
    }
    
    public Order(Long id, Long userId, String productName, Integer quantity, BigDecimal amount, LocalDateTime createTime, User user) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.createTime = createTime;
        this.user = user;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
} 