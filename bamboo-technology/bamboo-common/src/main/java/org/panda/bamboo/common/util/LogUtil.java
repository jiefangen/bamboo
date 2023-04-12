package org.panda.bamboo.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志组件工具类
 *
 * @author fangen
 */
public class LogUtil {

    private LogUtil() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void error(Class<?> clazz, Throwable t) {
        Logger logger = getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(t.getLocalizedMessage(), t);
        }
    }

    public static void error(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isErrorEnabled()) {
            logger.error(format, args);
        }
    }

    public static void warn(Class<?> clazz, Throwable t) {
        Logger logger = getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(t.getLocalizedMessage(), t);
        }
    }

    public static void warn(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isWarnEnabled()) {
            logger.warn(format, args);
        }
    }

    public static void info(Class<?> clazz, Throwable t) {
        Logger logger = getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(t.getLocalizedMessage(), t);
        }
    }

    public static void info(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    public static void debug(Class<?> clazz, Throwable t) {
        Logger logger = getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(t.getLocalizedMessage(), t);
        }
    }

    public static void debug(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(format, args);
        }
    }

    public static void trace(Class<?> clazz, Throwable t) {
        Logger logger = getLogger(clazz);
        if (logger.isTraceEnabled()) {
            logger.trace(t.getLocalizedMessage(), t);
        }
    }

    public static void trace(Class<?> clazz, String format, Object... args) {
        Logger logger = getLogger(clazz);
        if (logger.isTraceEnabled()) {
            logger.trace(format, args);
        }
    }

}
