package org.panda.tech.core.web.config.exception;

import org.panda.bamboo.common.exception.ExceptionEnum;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.core.web.restful.RestfulResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 *
 * @author fangen
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 方法参数校验异常统一处理
     *
     * @param ex 异常
     * @return 处理结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestfulResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = error.getField();
            String defaultErrMsg = error.getDefaultMessage();
            String entityName = StringUtil.firstToUpperCase(error.getObjectName());
            errorMessage.append(entityName).append("'s '").append(fieldName).append("': ").append(defaultErrMsg).append("; ");
        }
        LogUtil.error(getClass(), errorMessage.toString());
        return RestfulResult.failure(ExceptionEnum.PARAMETERS_REQUIRED.getCode(), ExceptionEnum.PARAMETERS_REQUIRED.getMessage());
    }
}
