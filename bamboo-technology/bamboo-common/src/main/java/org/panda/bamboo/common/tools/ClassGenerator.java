package org.panda.bamboo.common.tools;

/**
 * 类生成器
 */
public interface ClassGenerator {

    /**
     * 为指定接口或抽象类型生成一个简单的实现类或子类
     *
     * @param clazz 接口或抽象类型
     * @param <T>   接口或抽象类型
     * @return 实现类或子类，如果指定类型不是接口和抽象类，则返回该类型本身
     */
    <T> Class<? extends T> generateSimple(Class<T> clazz);

}
