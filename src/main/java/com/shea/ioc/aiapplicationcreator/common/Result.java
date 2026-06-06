package com.shea.ioc.aiapplicationcreator.common;

import com.shea.ioc.aiapplicationcreator.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 * @author : Shea.
 * @since : 2026/6/6 14:41
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private T data;
    private String msg;

    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(ErrorCode errorCode,String msg) {
        Result<T> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMsg(msg == null ? errorCode.getMessage() : msg);
        return result;
    }
}
