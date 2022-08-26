package org.panda.bamboo.common.enums;

import org.panda.bamboo.common.norm.IResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    private Integer code;
    private String message;
    
}
