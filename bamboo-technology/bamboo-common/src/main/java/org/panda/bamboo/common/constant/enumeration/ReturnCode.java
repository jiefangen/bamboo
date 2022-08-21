package org.panda.bamboo.common.constant.enumeration;

import lombok.Getter;

@Getter
public enum ReturnCode {
    /**
     * 操作成功
     */
    RC200(200, "success"),
    /**
     * 操作失败
     */
    RC500(500, "failure");

    private int code;
    private String message;
    ReturnCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
