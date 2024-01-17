package org.panda.bamboo.common.exception;

/**
 * 抽象编译时异常基类
 */
public abstract class AbstractBaseException extends Exception {

    private static final long serialVersionUID = 2615028757885274376L;

    private Object payload;

    public AbstractBaseException() {
        super();
    }

    public AbstractBaseException(String message) {
        super(message);
    }

    /**
     * 设置负载，用于在异常处理过程中向后续处理环节传递数据
     *
     * @param payload 负载
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPayload() {
        return (T) this.payload;
    }

}
