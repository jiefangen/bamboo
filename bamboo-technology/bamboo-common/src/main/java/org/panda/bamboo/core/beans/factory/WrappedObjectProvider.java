package org.panda.bamboo.core.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;

import java.util.function.Function;

/**
 * 对提供对象进行封包的对象提供者
 *
 * @param <T> 对象类型
 */
public class WrappedObjectProvider<T> implements ObjectProvider<T> {

    private ObjectProvider<T> delegate;
    private Function<T, T> wrapper;

    public WrappedObjectProvider(ObjectProvider<T> delegate, Function<T, T> wrapper) {
        this.delegate = delegate;
        this.wrapper = wrapper;
    }

    @Override
    public T getObject(Object... args) throws BeansException {
        T object = this.delegate.getObject(args);
        return this.wrapper.apply(object);
    }

    @Override
    public T getIfAvailable() throws BeansException {
        T object = this.delegate.getIfAvailable();
        return this.wrapper.apply(object);
    }

    @Override
    public T getIfUnique() throws BeansException {
        T object = this.delegate.getIfUnique();
        return this.wrapper.apply(object);
    }

    @Override
    public T getObject() throws BeansException {
        T object = this.delegate.getObject();
        return this.wrapper.apply(object);
    }

}
