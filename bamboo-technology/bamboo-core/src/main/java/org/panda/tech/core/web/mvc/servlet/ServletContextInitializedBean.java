package org.panda.tech.core.web.mvc.servlet;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * ServletContext初始化Bean
 *
 * @author fangen
 */
@Component
public class ServletContextInitializedBean implements ServletContextAware, ContextInitializedBean {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 在取得ServletContext时可能Spring容器中的Bean尚未完成初始化，所以等容器全部初始化完成后再进行下列动作
     *
     * @param context 容器上下文
     * @throws Exception
     */
    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {

    }

}
