package org.panda.bamboo.common.standard;

import lombok.Getter;

import java.io.Serializable;

import org.panda.bamboo.common.enums.ResultEnum;
import org.panda.bamboo.common.norm.IResult;

/**
 * 定义restful接口标准返回格式
 *
 * @author jiefangen
 **/
@Getter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    // 保护返回结构不被破坏，禁止外部实例化
    private Result(){};

    public static <T> Result<T> success(){
        return transform(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data){
        return transform(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }
    
    public static <T> Result<T> success(String message, T data){
        return transform(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static Result<?> failure(){
        return transform(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage(), null);
    }

    public static Result<?> failure(String message){
        return transform(ResultEnum.FAILURE.getCode(), message, null);
    }

    public static Result<?> failure(Integer code , String message){
        return transform(code, message, null);
    }
    
    public static Result<?> getFailure(IResult failedResult){
        return transform(failedResult.getCode(), failedResult.getMessage(), null);
    }

    private static <T> Result<T> transform(int code, String message, T data){
        Result<T> result = new Result<T>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

}
