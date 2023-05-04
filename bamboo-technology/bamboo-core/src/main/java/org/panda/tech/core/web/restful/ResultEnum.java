package org.panda.tech.core.web.restful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.bamboo.common.constant.Commons;

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
