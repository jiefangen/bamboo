package org.panda.tech.core.web.context;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * SpringWeb上下文工具类
 *
 * @author fangen
 */
public class SpringWebContext {

    private SpringWebContext() {
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    public static Locale getLocale() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getLocale();
        } else {
            return Locale.getDefault();
        }
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        return null;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletContext getServletContext() {
        return getSession().getServletContext();
    }

    /**
     * 设置request的属性
     *
     * @param name  属性名
     * @param value 属性值
     */
    public static void set(String name, Object value) {
        getRequest().setAttribute(name, value);
    }

    /**
     * 获取request的属性值
     *
     * @param name 属性名
     * @param <T>  属性类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String name) {
        return (T) getRequest().getAttribute(name);
    }

    /**
     * 移除request的属性
     *
     * @param name 属性名
     * @param <T>  属性类型
     * @return 被移除的属性值，没有该属性则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T remove(String name) {
        HttpServletRequest request = getRequest();
        Object value = request.getAttribute(name);
        if (value != null) {
            request.removeAttribute(name);
        }
        return (T) value;
    }

    /**
     * 设置属性至SESSION中
     *
     * @param name  属性名
     * @param value 属性值
     */
    public static void setToSession(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    /**
     * 从SESSION获取指定属性
     *
     * @param name 属性名
     * @param <T>  属性类型
     * @param <T>  属性类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFromSession(String name) {
        return (T) getSession().getAttribute(name);
    }

    /**
     * 从SESSION获取指定属性，如果不存在，则将指定默认值加入SESSION并返回
     *
     * @param name         属性名
     * @param defaultValue 默认属性值
     * @param <T>          属性类型
     * @return 属性值
     */
    public static <T> T getFromSession(String name, T defaultValue) {
        T value = getFromSession(name);
        if (value == null) {
            value = defaultValue;
            setToSession(name, value);
        }
        return value;
    }

    /**
     * 从SESSION获取指定属性，如果不存在，则执行指定默认值提供者，得到默认值加入SESSION并返回
     *
     * @param name                 属性名
     * @param defaultValueSupplier 默认属性值提供者
     * @param <T>                  属性类型
     * @return 属性值
     */
    public static <T> T getFromSession(String name, Supplier<T> defaultValueSupplier) {
        T value = getFromSession(name);
        if (value == null) {
            value = defaultValueSupplier.get();
            setToSession(name, value);
        }
        return value;
    }

    /**
     * 移除SESSION中的指定属性
     *
     * @param name 属性名
     * @param <T>  属性类型
     * @return 被移除的属性值，没有该属性则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeFromSession(String name) {
        HttpSession session = getSession();
        Object value = session.getAttribute(name);
        if (value != null) {
            session.removeAttribute(name);
        }
        return (T) value;
    }

    /**
     * 转换指定结果名为直接重定向的结果名
     *
     * @param result 结果名
     * @return 直接重定向的结果名
     */
    public static String toRedirectResult(String result) {
        return StringUtils.join("redirect:", result);
    }

    public static RequestMethod getRequestMethod() {
        String method = getRequest().getMethod().toUpperCase();
        return EnumUtils.getEnum(RequestMethod.class, method);
    }

    /**
     * 使当前session失效，下次再使用session时将重新创建新的session
     */
    public static void invalidateSession() {
        getSession().invalidate();
    }
}
