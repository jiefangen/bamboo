package org.panda.business.admin.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }
}
