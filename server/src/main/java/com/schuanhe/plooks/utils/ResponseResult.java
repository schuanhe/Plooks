package com.schuanhe.plooks.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @Author schuanhe  github： https://github.com/schuanhe
 * 为null的字符串不序列化
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    // 构造函数
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>(HttpStatus.OK.value(), "success", null);
    }

    /**
     * 成功响应
     * @param data 返回的数据
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(HttpStatus.OK.value(), "success", data);
    }

    /**
     * 成功响应
     * @param message 成功信息
     * @param data 返回的数据
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<T>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 失败 带
     * @param message 失败信息
     * @param <T> 泛型
     * @return 返回失败信息
     */
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<T>(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T> ResponseResult<T> fail(int code, String message) {
        return new ResponseResult<T>(code, message, null);
    }

    // 错误的响应
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    //错误的响应带错误代码
    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<T>(code, message, null);
    }

}