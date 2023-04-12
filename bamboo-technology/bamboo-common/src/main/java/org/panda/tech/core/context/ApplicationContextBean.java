package org.panda.tech.core.context;

import org.panda.tech.common.util.clazz.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextBean implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return SpringUtil.getBeanByName(applicationContext, beanName);
    }

    public static <T> T getBean(Class<T> type) {
        return SpringUtil.getBeanByDefaultName(applicationContext, type);
    }
}
