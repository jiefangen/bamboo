package org.panda.tech.core.web.mvc.view.exception;

import org.panda.bamboo.common.util.lang.CollectionUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AJAX访问异常
 */
public class AjaxException extends Exception {

    private static final long serialVersionUID = 5317130349189187298L;

    /**
     * 错误码-错误消息的映射
     */
    private Map<String, String> messages;

    /**
     * @param message 错误消息
     */
    public AjaxException(String message) {
        super(message);
    }

    /**
     * 构建含错误码的RPC异常对象
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public AjaxException(String code, String message) {
        super(message);
        this.messages = new HashMap<>();
        this.messages.put(code, message);
    }

    /**
     * 构建含多个错误的RPC异常对象
     *
     * @param messages 错误码-错误消息的映射集
     */
    public AjaxException(Map<String, String> messages) {
        this.messages = messages;
    }

    /**
     * @return 首个错误码
     */
    public String getCode() {
        if (this.messages != null) {
            return CollectionUtil.getFirst(this.messages.keySet(), null);
        }
        return null;
    }

    /**
     * @return 所有错误码
     */
    public Set<String> getCodes() {
        if (this.messages != null) {
            return this.messages.keySet();
        }
        return Collections.emptySet();
    }

    @Override
    public String getMessage() {
        return getMessage(getCode());
    }

    /**
     * 获取指定错误码的错误消息
     *
     * @param code 错误码
     * @return 错误消息
     */
    public String getMessage(String code) {
        if (this.messages != null && code != null) {
            return this.messages.get(code);
        }
        return null;
    }

}
