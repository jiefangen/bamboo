package org.panda.bamboo.common.exception;

/**
 * 抽象运行时异常基类
 */
public abstract class AbstractRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -7580996376952960084L;

    private int code;

    public AbstractRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
