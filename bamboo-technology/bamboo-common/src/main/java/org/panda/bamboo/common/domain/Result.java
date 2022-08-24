package org.panda.bamboo.common.domain;

import lombok.Getter;

import java.io.Serializable;

import org.panda.bamboo.common.constant.enums.ReturnCode;

/**
 * 定义restful接口标准返回格式
 *
 * @author jiefangen
 **/
@Getter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    private Result(){};

    public static Result getSuccess(){
        return transform(ReturnCode.RC200.getCode(), ReturnCode.RC200.getMessage(),null);
    }

    public static <T> Result<T> getSuccess(T data){
        return transform(ReturnCode.RC200.getCode(), ReturnCode.RC200.getMessage(), data);
    }

    public static Result getFailure(){
        return transform(ReturnCode.RC500.getCode(), ReturnCode.RC500.getMessage(), null);
    }

    public static Result getFailure(String message){
        return transform(ReturnCode.RC500.getCode(), message, null);
    }

    public static <T> Result<T> getFailure(int code , String message, T data){
        return transform(code, message, data);
    }

    private static <T> Result<T> transform(int code, String message, T data){
        Result<T> result = new Result();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

}
