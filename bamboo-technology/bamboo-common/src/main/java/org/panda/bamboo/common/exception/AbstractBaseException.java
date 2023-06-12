package org.panda.bamboo.common.exception;

/**
 * 抽象编译时异常基类
 */
public abstract class AbstractBaseException extends Exception {

    private static final long serialVersionUID = 6980402847581981301L;

    private Integer code;

    public AbstractBaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
