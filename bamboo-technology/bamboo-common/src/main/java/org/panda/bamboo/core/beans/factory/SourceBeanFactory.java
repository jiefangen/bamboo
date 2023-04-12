package org.panda.bamboo.core.beans.factory;

import org.springframework.beans.factory.BeanFactory;

/**
 * 可以获取非代理的原始Bean的Bean工厂
 *
 * @author fangen
 */
public interface SourceBeanFactory extends BeanFactory {

    <T> T getSourceBean(String name);

    <T> T getSourceBean(String name, Class<T> requiredType);

    <T> T getSourceBean(Class<T> requiredType);

}
