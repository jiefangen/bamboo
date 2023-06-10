package org.panda.bamboo.common.exception;

/**
 * 抽象编译时异常基类
 */
public abstract class AbstractBaseException extends Exception {

    private static final long serialVersionUID = -8278275368712652369L;

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
