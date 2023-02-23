package org.panda.core.spec.restful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.CommonConstant;
import org.panda.core.spec.IResult;

@Getter
@AllArgsConstructor
public enum ResultEnum implements IResult {
    /**
     * 操作成功
     */
	SUCCESS(CommonConstant.RESULT_SUCCESS, "success"),
    /**
     * 操作失败
     */
    FAILURE(CommonConstant.RESULT_FAILURE, "failure");

    private int code;
    private String message;
    
}
