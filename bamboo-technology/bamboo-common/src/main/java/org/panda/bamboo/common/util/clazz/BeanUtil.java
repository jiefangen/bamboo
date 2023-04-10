package org.panda.bamboo.common.util.clazz;

import org.apache.commons.lang3.ArrayUtils;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

/**
 * Bean工具类
 *
 * @author fangen
 */
public class BeanUtil {

    /**
     * 检查指定的2个bean是否相等，只根据id属性进行判断，没有id属性或id属性值都为null，则直接用bean的原始equals方法进行比较
     *
     * @param bean      比较的bean
     * @param otherBean 另一个比较的bean
     * @return true if 指定的2个bean相等, otherwise false
     */
    public static boolean equalsById(Object bean, Object otherBean) {
        if (bean == null || otherBean == null) {
            throw new NullPointerException();
        }
        Class<?> class1 = bean.getClass();
        Class<?> class2 = otherBean.getClass();
        if (!class1.isAssignableFrom(class2) || !class2.isAssignableFrom(class1)) { // 比较的两个bean的类型必须相同
            return false;
        }
        Long id1 = getPropertyValue(bean, "id");
        Long id2 = getPropertyValue(otherBean, "id");
        if (id1 == null && id2 == null) {
            return bean.equals(otherBean);
        }
        return id1 != null && id1.equals(id2);
    }

    /**
     * 获取指定bean对象的指定属性值。若该bean不存在指定属性，则返回null。属性名支持形如a.name的复杂对象属性名
     *
     * @param bean         bean对象
     * @param propertyName 属性名称
     * @param <T>          属性类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPropertyValue(Object bean, String propertyName) {
        try {
            String[] names = propertyName.split("\\.");
            return (T) getRefPropertyValue(bean, names);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取指定bean的指定逐级引用属性的值，如：bean.a.b.c
     *
     * @param bean          对象
     * @param propertyNames 逐级引用属性
     * @return 值
     * @throws IllegalAccessException    如果有一个属性的获取方法不可访问
     * @throws InvocationTargetException 如果有一个属性不存在
     * @throws NullPointerException      如果中间有一个属性的值为null
     */
    private static Object getRefPropertyValue(Object bean, String... propertyNames)
            throws IllegalAccessException, InvocationTargetException, NullPointerException {
        Object value = bean;
        for (String name : propertyNames) {
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(value.getClass(), name);
            if (pd != null) {
                value = pd.getReadMethod().invoke(value);
            }
        }
        return value;
    }

    /**
     * 设置指定bean对象的指定属性的值，忽略属性设置错误，不能设置则不设置
     *
     * @param bean         对象
     * @param propertyName 属性名
     * @param value        属性值
     * @return 是否设置成功，当指定属性不存在或无法设置值时返回false，否则返回true
     */
    public static boolean setPropertyValue(@Nullable Object bean, String propertyName, @Nullable Object value) {
        if (bean != null) {
            String[] names = propertyName.split("\\.");
            if (names.length > 1) {
                try {
                    bean = getRefPropertyValue(bean, ArrayUtils.subarray(names, 0, names.length - 1));
                    propertyName = names[names.length - 1];
                } catch (Exception e) {
                    return false; // 忽略属性设置错误，不能设置则不设置
                }
            }
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(bean.getClass(), propertyName);
            if (pd != null) {
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod != null) {
                    try {
                        writeMethod.invoke(bean, value);
                        return true;
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        return false; // 忽略属性设置错误，不能设置则不设置
                    }
                }
            }
        }
        return false;
    }

    /**
     * 直接设置指定bean的指定字段值
     *
     * @param bean  bean
     * @param name  字段名
     * @param value 字段值
     */
    public static void setFieldValue(Object bean, String name, Object value) {
        if (bean != null) {
            Class<?> type = bean.getClass();
            Field field = ClassUtil.findField(type, name);
            setFieldValue(bean, field, value);
        }
    }

    public static void setFieldValue(Object bean, Field field, Object value) {
        if (field != null) {
            boolean accessible = field.canAccess(bean);
            if (!accessible) {
                field.setAccessible(true);
            }
            try {
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                LogUtil.warn(BeanUtil.class, e);
            }
            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object bean, String name) {
        if (bean != null) {
            Field field = ClassUtil.findField(bean.getClass(), name);
            return (T) getFieldValue(bean, field);
        }
        return null;
    }

    public static Object getFieldValue(Object bean, Field field) {
        if (field != null) {
            try {
                boolean accessible = field.canAccess(bean);
                if (!accessible) {
                    field.setAccessible(true);
                }
                Object value = field.get(bean);
                if (!accessible) {
                    field.setAccessible(false);
                }
                return value;
            } catch (Exception e) {
                // 忽略所有异常
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object bean, Class<T> fieldType) {
        if (bean != null) {
            Field field = ClassUtil.findField(bean.getClass(), fieldType);
            return (T) getFieldValue(bean, field);
        }
        return null;
    }

    /**
     * 判断指定bean集合中是否包含指定bean，包含关系根据两个bean的id属性值是否相等进行判断
     *
     * @param collection bean集合
     * @param bean       比较bean
     * @return true if 指定集合中包含指定bean, otherwise false
     */
    public static boolean containsById(Collection<?> collection, Object bean) {
        for (Object o : collection) {
            if (equalsById(bean, o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除bean集合中的比较bean，如果指定bean集合中包含指定bean，则删除；
     *
     * @param collection bean集合
     * @param bean       比较bean
     */
    public static void removeById(Collection<?> collection, Object bean) {
        for (Object o : collection) {
            if (equalsById(bean, o)) {
                collection.remove(o);
                return;
            }
        }
    }

    /**
     * 获取指定bean集合中id属性值等于指定id的bean
     *
     * @param collection bean集合
     * @param id         id属性值
     * @return true if 指定bean集合中id属性值等于指定id的bean, otherwise false
     */
    public static Object getById(Collection<?> collection, long id) {
        try {
            for (Object o : collection) {
                Long oid = getPropertyValue(o, "id");
                if (oid != null && oid == id) {
                    return o;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 遍历指定bean的所有属性
     *
     * @param bean               Bean对象
     * @param consumer           遍历消费者
     * @param excludedProperties 排除的属性集
     */
    public static void loopProperties(Object bean, BiConsumer<String, Object> consumer, String... excludedProperties) {
        if (bean != null) {
            ClassUtil.loopPropertyDescriptors(bean.getClass(), pd -> {
                try {
                    String propertyName = pd.getName();
                    if (!"class".equals(propertyName) && !ArrayUtils.contains(excludedProperties, propertyName)) {
                        Method readMethod = pd.getReadMethod();
                        if (readMethod != null) {
                            Object propertyValue = readMethod.invoke(bean);
                            if (propertyValue != null) {
                                consumer.accept(propertyName, propertyValue);
                            }
                        }
                    }
                } catch (Exception e) { // 出现任何异常不做任何处理
                }
            });
        }
    }

    /**
     * 将指定bean对象转换为Map，其条目关键字为bean对象的属性名，条目取值为该属性的值
     *
     * @param bean               bean对象
     * @param excludedProperties 排除的属性
     * @return Map结果对象
     */
    public static Map<String, Object> toMap(Object bean, String... excludedProperties) {
        Map<String, Object> map = new HashMap<>();
        loopProperties(bean, map::put, excludedProperties);
        return map;
    }

    /**
     * 从指定Map中取值设置到指定bean对象中
     *
     * @param bean         bean对象
     * @param map          Map
     * @param excludedKeys 排除的关键字
     */
    public static void fromMap(Object bean, Map<String, Object> map, String... excludedKeys) {
        for (Entry<String, Object> entry : map.entrySet()) {
            try {
                String key = entry.getKey();
                if (!"class".equals(key) && !ArrayUtils.contains(excludedKeys, key)) {
                    Object value = entry.getValue();
                    PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(bean.getClass(), key);
                    if (pd != null) {
                        Method writeMethod = pd.getWriteMethod();
                        if (writeMethod != null) {
                            writeMethod.invoke(bean, value);
                        }
                    }
                }
            } catch (Exception e) { // 出现任何异常不做任何处理
            }
        }
    }

    /**
     * 用指定Map生成指定类型的bean对象
     *
     * @param map          Map
     * @param clazz        bean类型
     * @param excludedKeys 排除的关键字
     * @param <T>          bean类型
     * @return bean对象
     * @throws ReflectiveOperationException bean类型没有无参构造函数
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> clazz, String... excludedKeys)
            throws ReflectiveOperationException {
        T bean = clazz.getConstructor().newInstance();
        fromMap(bean, map, excludedKeys);
        return bean;
    }

    /**
     * 获取静态属性表达式所表示的静态属性值，静态属性表达式形如：@org.truenewx.tnxjee.core.util.DateUtil@SHORT_DATE_PATTERN <br>
     * 如果表达式错误或所表示的属性为非静态或不可访问 ，则返回null
     *
     * @param propertyExpression 静态属性表达式
     * @return 静态属性值
     */
    public static Object getStaticPropertyExpressionValue(String propertyExpression) {
        String[] names = propertyExpression.split("@");
        if (names.length != 3 || names[0].length() > 0) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(names[1]);
            Field field = clazz.getField(names[2]);
            return field.get(null);
        } catch (Exception e) {
            // 忽略所有异常
        }
        return null;
    }

    /**
     * 判断指定的bean对象是否具有指定属性的写方法
     *
     * @param bean          对象
     * @param propertyName  属性名
     * @param propertyClass 属性类型
     * @return true if 指定的bean对象具有指定属性的写方法, otherwise false
     */
    public static boolean hasWritableProperty(Object bean, String propertyName, Class<?> propertyClass) {
        try {
            String methodName = "set" + StringUtil.firstToUpperCase(propertyName);
            bean.getClass().getMethod(methodName, propertyClass);
            return true;
        } catch (SecurityException | NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * 将指定源对象中的简单属性的值复制到指定目标对象中，如果目标对象中无相应属性则忽略。 简单属性包括：原始类型，字符串，数字，日期，URI，URL，Locale
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copySimpleProperties(Object source, Object target) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        Class<?> targetClass = target.getClass();
        for (PropertyDescriptor pd : propertyDescriptors) {
            try {
                if (BeanUtils.isSimpleValueType(pd.getPropertyType())) {
                    String name = pd.getDisplayName();
                    if (!"class".equals(name)) {
                        PropertyDescriptor writePd = BeanUtils.getPropertyDescriptor(targetClass, name);
                        if (writePd != null) {
                            Method writeMethod = writePd.getWriteMethod();
                            if (writeMethod != null) {
                                Object value = pd.getReadMethod().invoke(source);
                                writeMethod.invoke(target, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.error(BeanUtil.class, e);
            }
        }
    }

    public static <T> T getTargetSource(T bean) {
        try {
            if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
                Advised proxy = (Advised) bean;
                return (T) proxy.getTargetSource().getTarget();
            }
        } catch (Exception e) {
            LogUtil.error(BeanUtil.class, e);
        }
        return bean;
    }

    public static void copyFields(Object source, Object target, String... ignoreFields) {
        if (source != null && target != null) {
            ClassUtil.loopDynamicFields(source.getClass(), field -> {
                String fieldName = field.getName();
                if (!ArrayUtils.contains(ignoreFields, fieldName)) {
                    Object fieldValue = getFieldValue(source, field);
                    setFieldValue(target, fieldName, fieldValue);
                }
                return true;
            });
        }
    }

    /**
     * 复制字段值到指定类型的新建实例中
     *
     * @param source       源对象
     * @param targetClass  目标对象类型，必须具有无参构造函数
     * @param ignoreFields 忽略的字段名称集
     * @param <T>          目标对象类型
     * @return 新建并获得复制字段值的实例
     */
    public static <T> T copyFieldsToNewInstance(Object source, Class<T> targetClass, String... ignoreFields) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getConstructor().newInstance();
            copyFields(source, target, ignoreFields);
            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object source, @Nullable Class<T> targetClass) {
        if (targetClass == null || targetClass.isInstance(source)) {
            return (T) source;
        }
        return copyFieldsToNewInstance(source, targetClass);
    }

}
