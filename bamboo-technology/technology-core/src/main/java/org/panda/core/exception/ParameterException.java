package org.panda.core.exception;

import org.panda.bamboo.common.exception.AbstractRuntimeException;

/**
 * 参数异常
 */
public class ParameterException extends AbstractRuntimeException {
    private static final long serialVersionUID = 6743671808524987765L;

    public ParameterException(String message) {
        super(5101, message);
    }

    public ParameterException() {
        super(5101, "Parameter exception");
    }
}
