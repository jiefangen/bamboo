package org.panda.core.spec.restful;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.core.spec.IResult;

@Getter
@AllArgsConstructor
public enum ResultEnum implements IResult {
    /**
     * 操作成功
     */
	SUCCESS(2001, "success"),
    /**
     * 操作失败
     */
    FAILURE(5001, "failure");

    private int code;
    private String message;
    
}
