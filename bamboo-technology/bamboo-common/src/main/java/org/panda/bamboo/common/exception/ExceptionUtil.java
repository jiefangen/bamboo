package org.panda.bamboo.common.exception;

/**
 * 异常工具类
 */
public class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static RuntimeException toRuntimeException(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        Throwable cause = t.getCause();
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        }
        return new RuntimeException(t);
    }

    public static void throwRuntimeException(Throwable t) {
        if (t != null) {
            throw toRuntimeException(t);
        }
    }
}
