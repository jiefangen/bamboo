package org.panda.bamboo.common.exception;

/**
 * 抽象运行时异常基类
 */
public abstract class AbstractRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2300174770930974698L;

    private int code;
    protected String message;

    public AbstractRuntimeException(String message) {
        super(message);
    }

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

    public String getMessage() {
        return message;
    }
}
