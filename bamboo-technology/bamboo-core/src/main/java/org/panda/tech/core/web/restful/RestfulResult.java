package org.panda.tech.core.web.restful;

import lombok.Getter;

import java.io.Serializable;

/**
 * 定义restful接口标准返回格式
 *
 * @author fangen
 **/
@Getter
public class RestfulResult<T> implements Serializable {
    private static final long serialVersionUID = 8037544993836312672L;

    private int code;
    private String message;
    private T data;

    // 保护返回结构不被破坏，禁止外部实例化
    private RestfulResult() {}

    public static <T> RestfulResult<T> success() {
        return transform(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> RestfulResult<T> success(T data) {
        return transform(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }
    
    public static <T> RestfulResult<T> success(String message, T data) {
        return transform(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static RestfulResult<?> failure() {
        return transform(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage(), null);
    }

    public static RestfulResult<?> failure(String message) {
        return transform(ResultEnum.FAILURE.getCode(), message, null);
    }

    public static RestfulResult<?> failure(int code, String message) {
        return transform(code, message, null);
    }
    
    public static RestfulResult<?> getFailure(Result failedResult) {
        return transform(failedResult.getCode(), failedResult.getMessage(), null);
    }

    private static <T> RestfulResult<T> transform(int code, String message, T data) {
        RestfulResult<T> result = new RestfulResult<>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

}
