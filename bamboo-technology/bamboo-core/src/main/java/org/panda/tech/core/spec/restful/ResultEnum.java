package org.panda.tech.core.spec.restful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.tech.common.constant.Commons;
import org.panda.tech.core.spec.Result;

@Getter
@AllArgsConstructor
public enum ResultEnum implements Result {
    /**
     * 操作成功
     */
	SUCCESS(Commons.RESULT_SUCCESS_CODE, Commons.RESULT_SUCCESS),
    /**
     * 操作失败
     */
    FAILURE(Commons.RESULT_FAILURE_CODE, Commons.RESULT_FAILURE);

    private int code;
    private String message;
    
}
