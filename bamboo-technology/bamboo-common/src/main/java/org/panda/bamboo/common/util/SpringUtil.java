package org.panda.bamboo.common.util;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.lang.ArrayUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Spring工具类
 *
 * @author fangen
 */
public class SpringUtil {

    private SpringUtil() {
    }

    public static String getActiveProfile(Environment env) {
        String[] profiles = env.getActiveProfiles();
        return ArrayUtil.get(profiles, 0);
    }

    public static String getActiveProfile(ApplicationContext context) {
        return getActiveProfile(context.getEnvironment());
    }

    public static boolean isActiveProfile(Environment env, String profile) {
        String[] profiles = env.getActiveProfiles();
        return ArrayUtils.contains(profiles, profile);
    }

    public static boolean isActiveProfile(ApplicationContext context, String profile) {
        return isActiveProfile(context.getEnvironment(), profile);
    }

    /**
     * 从Spring容器中获取指定名称的bean
     *
     * @param context  Spring容器上下文
     * @param beanName bean名称
     * @param <T>      bean类型
     * @return bean对象，如果找不到则返回null
     */
    public static <T> T getBeanByName(ApplicationContext context, String beanName) {
        try {
            return (T) context.getBean(beanName);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 按照默认命名规则，从Spring容器上下文中获取指定类型的bean。默认命名规则为类名首字母小写
     *
     * @param context   Spring容器上下文
     * @param beanClass bean类型
     * @param <T>       bean类型
     * @return Spring容器上下文中指定类型的bean
     */
    public static <T> T getBeanByDefaultName(ApplicationContext context, Class<T> beanClass) {
        try {
            return context.getBean(StringUtil.firstToLowerCase(beanClass.getSimpleName()), beanClass);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 从Spring容器上下文中获取指定类型的第一个bean，优先获取bean名称为默认命名规则下的名称的bean，
     * 如果没有则获取Spring容器中默认顺序下的第一个bean。 该方法一般用于在确知Spring容器中只有一个指定类型的bean，或不关心指定类型实现时
     *
     * @param context          Spring容器上下文
     * @param beanClass        bean类型
     * @param exclusiveClasses 排除的类型。该类型（一般为指定bean类型的子类）的bean不作为有效结果对象返回
     * @param <T>              bean类型
     * @return Spring容器上下文中获取指定类型的第一个bean
     */
    public static <T> T getFirstBeanByClass(ApplicationContext context, Class<T> beanClass,
            Class<?>... exclusiveClasses) {
        if (context == null) {
            return null;
        }
        T bean = getBeanByDefaultName(context, beanClass);
        if (bean != null && !ArrayUtils.contains(exclusiveClasses, bean.getClass())) {
            return bean;
        }
        String[] beanNames = context.getBeanNamesForType(beanClass);
        for (String beanName : beanNames) {
            T obj = context.getBean(beanName, beanClass);
            if (!ArrayUtils.contains(exclusiveClasses, obj.getClass())) {
                return obj;
            }
        }
        return getFirstBeanByClass(context.getParent(), beanClass, exclusiveClasses);
    }

}
