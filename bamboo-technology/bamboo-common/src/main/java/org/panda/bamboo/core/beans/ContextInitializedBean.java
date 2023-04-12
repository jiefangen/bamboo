package org.panda.bamboo.core.beans;

import org.springframework.context.ApplicationContext;

/**
 * 在容器初始化完成后执行操作的bean
 *
 * @author fangen
 */
public interface ContextInitializedBean {

    /**
     * 容器初始化完成后执行的动作
     *
     * @param context 容器上下文
     * @throws Exception 如果处理过程出现错误
     */
    void afterInitialized(ApplicationContext context) throws Exception;

}
