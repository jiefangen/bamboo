package org.panda.bamboo.common.util.clazz;

import org.panda.bamboo.common.constant.basic.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * 获取指定类型的默认值
 */
public class ClassDefault {

    /**
     * 原生默认值
     */
    private static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = new HashMap<>();
    /**
     * 包装类默认值
     */
    private static final Map<Class<?>, Object> OBJECT_DEFAULTS = new HashMap<>();

    private ClassDefault() {
    }

    static {
        PRIMITIVE_DEFAULTS.put(boolean.class, false);
        PRIMITIVE_DEFAULTS.put(char.class, '\0');
        PRIMITIVE_DEFAULTS.put(byte.class, (byte) 0);
        PRIMITIVE_DEFAULTS.put(short.class, (short) 0);
        PRIMITIVE_DEFAULTS.put(int.class, 0);
        PRIMITIVE_DEFAULTS.put(long.class, 0L);
        PRIMITIVE_DEFAULTS.put(float.class, 0.0f);
        PRIMITIVE_DEFAULTS.put(double.class, 0.0d);
        OBJECT_DEFAULTS.put(String.class, Strings.EMPTY);
        OBJECT_DEFAULTS.put(Byte.class, (byte) 0);
        OBJECT_DEFAULTS.put(Character.class, (char) 0);
        OBJECT_DEFAULTS.put(Short.class, (short) 0);
        OBJECT_DEFAULTS.put(Integer.class, 0);
        OBJECT_DEFAULTS.put(Long.class, 0L);
        OBJECT_DEFAULTS.put(Float.class, 0.0f);
        OBJECT_DEFAULTS.put(Double.class, 0.0d);
        OBJECT_DEFAULTS.put(Boolean.class, Boolean.FALSE);
        OBJECT_DEFAULTS.put(BigDecimal.class, BigDecimal.ZERO);
        OBJECT_DEFAULTS.put(BigInteger.class, BigInteger.ZERO);
        OBJECT_DEFAULTS.put(Locale.class, Locale.getDefault());
        OBJECT_DEFAULTS.put(Currency.class, Currency.getInstance(Locale.getDefault()));
        OBJECT_DEFAULTS.put(Charset.class, StandardCharsets.UTF_8);
    }

    public static <T> T visit(Class<T> clazz, boolean nullObject) {
        if (clazz.isEnum()) {
            return clazz.getEnumConstants()[0];
        }
        if (Date.class.isAssignableFrom(clazz)) {
            return (T) new Date();
        }
        if (Temporal.class.isAssignableFrom(clazz)) {
            try {
                return (T) clazz.getMethod("now").invoke(null);
            } catch (ReflectiveOperationException e) {
                return null;
            }
        }
        T result = (T) PRIMITIVE_DEFAULTS.get(clazz);
        if (result == null && !nullObject) {
            result = (T) OBJECT_DEFAULTS.get(clazz);
            if (result == null) {
                try {
                    result = clazz.getConstructor().newInstance();
                } catch (Exception e) {
                    // 忽略所有异常
                }
            }
        }
        return result;
    }

}
