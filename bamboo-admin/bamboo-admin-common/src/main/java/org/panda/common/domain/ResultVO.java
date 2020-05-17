package org.panda.common.domain;

import lombok.Getter;

import java.io.Serializable;
/**
 * 返回结果对象
 *
 * @author jiefangen
 * @since JDK 1.8  2020/5/16
 **/
@Getter
public class ResultVO implements Serializable {
    private static final long serialVersionUID = 6326226541206554205L;

    private Integer code;

    private String message;

    private Object data;

    private ResultVO(){};

    public static ResultVO getSucess(){
        return transform(ResultConstant.SUCESS, ResultConstant.SUCESS_MSG,null);
    }

    public static ResultVO getSucess(String message){
        return transform(ResultConstant.SUCESS, message,null);
    }

    public static ResultVO getSucess(Object data){
        return transform(ResultConstant.SUCESS, ResultConstant.SUCESS_MSG, data);
    }

    public static ResultVO getFailure(){
        return transform(ResultConstant.FAILURE, ResultConstant.FAILURE_MSG, null);
    }

    public static ResultVO getFailure(Integer code ,String message){
        return transform(code, message, null);
    }

    public static ResultVO getFailure(Integer code ,String message, Object data){
        return transform(code, message, data);
    }

    private static ResultVO transform(Integer code, String message, Object data){
        ResultVO result = new ResultVO();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

}
