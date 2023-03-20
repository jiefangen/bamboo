package org.panda.core.spec.restful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.CommonConstant;
import org.panda.core.spec.Result;

@Getter
@AllArgsConstructor
public enum ResultEnum implements Result {
    /**
     * 操作成功
     */
	SUCCESS(CommonConstant.RESULT_SUCCESS_CODE, CommonConstant.RESULT_SUCCESS),
    /**
     * 操作失败
     */
    FAILURE(CommonConstant.RESULT_FAILURE_CODE, CommonConstant.RESULT_FAILURE);

    private int code;
    private String message;
    
}
