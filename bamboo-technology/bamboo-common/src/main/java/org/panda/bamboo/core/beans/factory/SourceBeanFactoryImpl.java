package org.panda.bamboo.core.beans.factory;

import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

/**
 * 源Bean工厂实现
 *
 * @author fangen
 */
@Component
public class SourceBeanFactoryImpl implements SourceBeanFactory, BeanFactoryAware {

    private BeanFactory delegate;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.delegate = beanFactory;
    }

    @Override
    public <T> T getSourceBean(String name) {
        T bean = (T) this.delegate.getBean(name);
        return BeanUtil.getTargetSource(bean);
    }

    @Override
    public <T> T getSourceBean(String name, Class<T> requiredType) {
        T bean = this.delegate.getBean(name, requiredType);
        return BeanUtil.getTargetSource(bean);
    }

    @Override
    public <T> T getSourceBean(Class<T> requiredType) {
        T bean = this.delegate.getBean(requiredType);
        return BeanUtil.getTargetSource(bean);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return this.delegate.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return this.delegate.getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return this.delegate.getBean(requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return this.delegate.getBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return this.delegate.getBean(requiredType, args);
    }

    @Override
    public boolean containsBean(String name) {
        return this.delegate.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return this.delegate.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return this.delegate.isPrototype(name);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> targetType)
            throws NoSuchBeanDefinitionException {
        return this.delegate.isTypeMatch(name, targetType);
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch)
            throws NoSuchBeanDefinitionException {
        return this.isTypeMatch(name, typeToMatch);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return this.delegate.getType(name);
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit)
            throws NoSuchBeanDefinitionException {
        return this.delegate.getType(name, allowFactoryBeanInit);
    }

    @Override
    public String[] getAliases(String name) {
        return this.delegate.getAliases(name);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return this.delegate.getBeanProvider(requiredType);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return this.delegate.getBeanProvider(requiredType);
    }

}
