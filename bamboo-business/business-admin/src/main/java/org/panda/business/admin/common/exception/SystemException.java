package org.panda.business.admin.common.exception;

import org.panda.bamboo.common.exception.business.BusinessException;

/**
 * 系统模块异常基类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/6/14
 */
public class SystemException extends BusinessException {

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(int code, String message) {
        super(code, message);
    }

}
