package com.example.common.entity;

/**
 * 通用响应结果类
 * 
 * 这个类是整个系统的统一响应格式，用于封装后端返回给前端的数据。
 * 采用泛型设计，可以适应不同类型的响应数据。
 * 
 * @param <T> 响应数据的类型
 */
public class Result<T> {
    /**
     * 响应状态码
     * 200 - 成功
     * 其他值 - 各种错误状态
     */
    private Integer code;
    
    /**
     * 响应消息
     * 用于描述请求处理结果
     */
    private String message;
    
    /**
     * 响应的实际数据
     */
    private T data;
    
    /**
     * 无参构造函数
     */
    public Result() {
    }
    
    /**
     * 带参数的构造函数
     * 
     * @param code 状态码
     * @param message 响应消息
     * @param data 响应数据
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 获取状态码
     * 
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 设置状态码
     * 
     * @param code 状态码
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
    /**
     * 获取响应消息
     * 
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置响应消息
     * 
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取响应数据
     * 
     * @return 响应数据
     */
    public T getData() {
        return data;
    }
    
    /**
     * 设置响应数据
     * 
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 创建成功响应，包含数据
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功的响应结果，状态码为200
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 创建不包含数据的成功响应
     * 
     * @param <T> 数据类型
     * @return 不包含数据的成功响应
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 创建错误响应，指定错误码和错误消息
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应，不包含数据
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 创建包含默认错误码(500)的错误响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应，状态码为500
     */
    public static <T> Result<T> fail(String message) {
        return fail(500, message);
    }
} 