package org.panda.tech.data.codegen;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.ClassUtil;
import org.panda.tech.data.model.entity.Entity;

import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * 基于模型的生成器支持
 *
 * @author fangen
 */
public abstract class ModelBasedGeneratorSupport {

    protected static String getModule(String basePackage, Class<?> clazz) {
        String packageName = clazz.getPackageName();
        if (packageName.length() > basePackage.length()) {
            String module = packageName.substring(basePackage.length() + 1);
            int index = module.indexOf(Strings.DOT);
            if (index > 0) { // 如果有多级子包，也只取基础包的直接下级包作为模块名
                module = module.substring(0, index);
            }
            return module;
        }
        return null;
    }

    protected static void generate(String modelBasePackage, BiConsumer<String, Class<? extends Entity>> consumer,
            String... modules) {
        if (ArrayUtils.isEmpty(modules)) {
            Collection<Class<?>> classes = ClassUtil.findClasses(modelBasePackage, Entity.class);
            for (Class<?> clazz : classes) {
                Class<? extends Entity> entityClass = (Class<? extends Entity>) clazz;
                String module = getModule(modelBasePackage, entityClass);
                consumer.accept(module, entityClass);
            }
        } else {
            for (String module : modules) {
                String modulePackageName = modelBasePackage + Strings.DOT + module;
                Collection<Class<?>> classes = ClassUtil.findClasses(modulePackageName, Entity.class);
                for (Class<?> clazz : classes) {
                    Class<? extends Entity> entityClass = (Class<? extends Entity>) clazz;
                    consumer.accept(module, entityClass);
                }
            }
        }
    }

    protected String modelBasePackage;

    public ModelBasedGeneratorSupport(String modelBasePackage) {
        this.modelBasePackage = modelBasePackage;
    }

    protected final String getModule(Class<?> clazz) {
        return getModule(this.modelBasePackage, clazz);
    }

}
