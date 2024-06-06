package org.panda.business.helper.app.common.exception;

import org.panda.tech.core.exception.business.BusinessException;

/**
 * 系统模块异常基类
 *
 * @author fangen
 * @since 2020/6/14
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
