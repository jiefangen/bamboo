package org.panda.bamboo.common.exception;

/**
 * 抽象运行时异常基类
 * 仅作为标识，在进行异常处理时便于判断
 */
public abstract class AbstractRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3485711626418585749L;

    private Object payload;

    public AbstractRuntimeException() {
        super();
    }

    public AbstractRuntimeException(String message) {
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
