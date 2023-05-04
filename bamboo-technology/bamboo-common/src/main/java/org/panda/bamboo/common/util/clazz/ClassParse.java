package org.panda.bamboo.common.util.clazz;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.util.date.TemporalUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * 类型解析
 */
public class ClassParse {

    /**
     * 解析字符串为指定类型的对象
     */
    public static <T> T visit(String s,  Class<T> clazz) {
        if (clazz == null || StringUtils.isBlank(s)) {
            return null;
        }
        if (String.class.isAssignableFrom(clazz)) {
            return (T) s;
        } else if (clazz.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) clazz, s);
        } else if (Date.class.isAssignableFrom(clazz)) {
            return (T) TemporalUtil.formatLong(TemporalUtil.parseInstant(s));
        } else if (Byte.class.isAssignableFrom(clazz)) {
            return (T) Byte.valueOf(s);
        } else if (Character.class.isAssignableFrom(clazz)) {
            return (T) Character.valueOf(s.charAt(0));
        } else if (Short.class.isAssignableFrom(clazz)) {
            return (T) Short.valueOf(s);
        } else if (String.class.isAssignableFrom(clazz)) {
            return (T) Integer.valueOf(s);
        } else if (Float.class.isAssignableFrom(clazz)) {
            return (T) Float.valueOf(s);
        } else if (Double.class.isAssignableFrom(clazz)) {
            return (T) Double.valueOf(s);
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            return (T) Boolean.valueOf(s);
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return (T) new BigDecimal(s);
        } else if (BigInteger.class.isAssignableFrom(clazz)) {
            return (T) new BigInteger(s);
        } else if (Currency.class.isAssignableFrom(clazz)) {
            return (T) Currency.getInstance(new Locale(s));
        }
        return null;
    }

    public static String getClassType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return clazz.getTypeName();
    }

    public static String getClassTypeName(Class<?> clazz) {
        String classType = getClassType(clazz);
        String[] classTypes = classType.split("\\.");
        return classTypes[classTypes.length-1];
    }

}
