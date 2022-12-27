package org.panda.bamboo.common.exception;

/**
 * 抽象编译时异常基类
 */
public abstract class AbstractBaseException extends Exception {
    private static final long serialVersionUID = -1582879827218948396L;
    private int code;

    public AbstractBaseException(int code, String message) {
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
