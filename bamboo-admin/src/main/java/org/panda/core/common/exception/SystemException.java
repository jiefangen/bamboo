package org.panda.core.common.exception;

/**
 * 系统模块异常基类
 *
 * @author jiefangen
 * @since JDK 1.8  2020/6/14
 */
public class SystemException extends Exception{
    public SystemException(){
        super();
    }

    public SystemException(String message){
        super(message);
    }

    public SystemException(Throwable cause){
        super(cause);
    }

    public SystemException(String message, Throwable cause){
        super(message, cause);
    }
}
