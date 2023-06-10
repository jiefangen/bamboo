package org.panda.bamboo.common.util.lang;

import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数学工具类
 *
 * @author fangen
 */
public class MathUtil {
    /**
     * 数值100
     */
    public static final BigDecimal HUNDRED = new BigDecimal(100);

    private MathUtil() {
    }

    /**
     * 获取随机双精度浮点数
     *
     * @param min 最小值
     * @param max 最大值（不包含）
     * @return 随机双精度浮点数
     */
    public static double randomDouble(double min, double max) {
        double d = Math.random();
        return min + (max - min) * d;
    }

    /**
     * 获取随机字节数
     *
     * @param min 最小值
     * @param max 最大值（不包含）
     * @return 随机字节数
     */
    public static byte randomByte(byte min, byte max) {
        return (byte) randomDouble(min, max);
    }

    /**
     * 获取随机整数
     *
     * @param min 最小值
     * @param max 最大值（不包含）
     * @return 随机整数
     */
    public static int randomInt(int min, int max) {
        return (int) randomDouble(min, max);
    }

    /**
     * 获取随机长整数
     *
     * @param min 最小值
     * @param max 最大值（不包含）
     * @return 随机长整数
     */
    public static long randomLong(long min, long max) {
        return (long) randomDouble(min, max);
    }

    /**
     * 以指定中奖几率抽奖，返回是否中奖
     *
     * @param probability 中奖几率
     * @return true if 中奖, otherwise false
     */
    public static boolean drawLottery(double probability) {
        return randomDouble(0, 1) < probability;
    }

    /**
     * 解析转换指定字符串为十进制数字，如果字符串不是合法的十进制数字格式，则返回null
     *
     * @param s 字符串
     * @return 转换后的十进制数字
     */
    public static BigDecimal parseDecimal(String s) {
        return parseDecimal(s, null);
    }

    /**
     * 解析转换指定字符串为十进制数字，如果字符串不是十进制数字格式，则返回指定默认值
     *
     * @param s            字符串
     * @param defaultValue 默认值
     * @return 转换后的十进制数字
     */
    public static BigDecimal parseDecimal(String s, BigDecimal defaultValue) {
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换指定字符串为长整型数字，如果字符串不是长整型数字格式，则返回0
     *
     * @param s 字符串
     * @return 转换后的长整型数字
     */
    public static long parseLong(String s) {
        return parseLong(s, 0);
    }

    /**
     * 解析转换指定字符串为长整型数字，如果字符串不是整型数字格式，则返回指定默认值
     *
     * @param s            字符串
     * @param defaultValue 默认值
     * @return 转换后的长整型数字
     */
    public static long parseLong(String s, long defaultValue) {
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long parseLongObject(String s, Long defaultValue) {
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long parseLongObject(String s) {
        return parseLongObject(s, null);
    }

    /**
     * 以指定分隔符拆分指定字符串，并解析为长整数数组
     *
     * @param s         字符串
     * @param separator 分隔符
     * @return 长整数数组
     */
    public static long[] parseLongArray(String s, String separator) {
        if (StringUtils.isNotBlank(s)) {
            String[] array = s.split(separator);
            long[] result = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = parseLong(array[i]);
            }
            return result;
        }
        return new long[0];
    }

    /**
     * 以指定分隔符拆分指定字符串，并解析为长整数对象数组
     *
     * @param s         字符串
     * @param separator 分隔符
     * @return 长整数数组
     */
    public static Long[] parseLongObjectArray(String s, String separator) {
        if (StringUtils.isNotBlank(s)) {
            String[] array = s.split(separator);
            Long[] result = new Long[array.length];
            for (int i = 0; i < array.length; i++) {
                try {
                    result[i] = Long.valueOf(array[i]);
                } catch (NumberFormatException e) {
                    result[i] = null; // 无法解析时结果为null
                }
            }
            return result;
        }
        return new Long[0];
    }

    /**
     * 以指定分隔符拆分指定字符串，并解析为长整数集合
     *
     * @param s         字符串
     * @param separator 分隔符
     * @return 长整数数组
     */
    public static List<Long> parseLongList(String s, String separator) {
        if (StringUtils.isNotBlank(s)) {
            List<Long> result = new ArrayList<>();
            String[] array = s.split(separator);
            for (String e : array) {
                Long value = parseLongObject(e);
                if (value != null) {
                    result.add(value);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * 解析转换指定字符串为整型数字，如果字符串不是整型数字格式，则返回0
     *
     * @param s 字符串
     * @return 转换后的整型数字
     */
    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    /**
     * 解析转换指定字符串为整型数字，如果字符串不是整型数字格式，则返回指定默认值
     *
     * @param s            字符串
     * @param defaultValue 默认值
     * @return 转换后的整型数字
     */
    public static int parseInt(String s, int defaultValue) {
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Integer parseInteger(String s) {
        return parseInteger(s, null);
    }

    public static Integer parseInteger(String s, Integer defaultValue) {
        if (StringUtils.isBlank(s)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 以指定分隔符拆分指定字符串，并解析为整数数组
     *
     * @param s         字符串
     * @param separator 分隔符
     * @return 整数数组
     */
    public static int[] parseIntArray(String s, String separator) {
        if (StringUtils.isNotBlank(s)) {
            String[] array = s.split(separator);
            int[] result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = parseInt(array[i]);
            }
            return result;
        }
        return new int[0];
    }

    /**
     * 以指定分隔符拆分指定字符串，并解析为整数对象数组
     *
     * @param s         字符串
     * @param separator 分隔符
     * @return 整数对象数组
     */
    public static Integer[] parseIntegerArray(String s, String separator) {
        if (StringUtils.isNotBlank(s)) {
            String[] array = s.split(separator);
            Integer[] result = new Integer[array.length];
            for (int i = 0; i < array.length; i++) {
                try {
                    result[i] = Integer.valueOf(array[i]);
                } catch (NumberFormatException e) {
                    result[i] = null; // 解析错误则对应位置为null
                }
            }
            return result;
        }
        return new Integer[0];
    }

    /**
     * 格式化指定数字对象
     *
     * @param number    数字对象
     * @param minScale  最小精度
     * @param maxScale  最大精度
     * @param toPercent 是否转换为百分比
     * @return 格式化后的字符串
     */
    public static String format(Number number, int minScale, int maxScale, boolean toPercent) {
        if (number == null) {
            return "";
        }
        NumberFormat format = toPercent ? NumberFormat.getPercentInstance() : NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(minScale);
        format.setMaximumFractionDigits(maxScale);
        return format.format(number);
    }

    public static String format(Number number, String pattern) {
        return new DecimalFormat(pattern).format(number);
    }


    public static int minValueIndexOf(double[] array) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (min > array[i]) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    public static int maxValueIndexOf(double[] array) {
        double max = Double.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    public static int minValueIndexOf(Number[] array) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (min > array[i].doubleValue()) {
                min = array[i].doubleValue();
                index = i;
            }
        }
        return index;
    }

    public static int maxValueIndexOf(Number[] array) {
        double max = Double.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (max < array[i].doubleValue()) {
                max = array[i].doubleValue();
                index = i;
            }
        }
        return index;
    }

    public static byte[] int2Bytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    public static int bytes2Int(byte[] bytes, int offset) {
        return (((bytes[offset] & 0xFF) << 24) | ((bytes[offset + 1] & 0xFF) << 16) | ((bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF));
    }

    public static int hex2Int(String hex) {
        return hex2Int(hex, 0);
    }

    public static int hex2Int(String hex, int defaultValue) {
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T parse(String s, Class<T> type) {
        if (type == BigDecimal.class) {
            return (T) new BigDecimal(s);
        } else if (type == BigInteger.class) {
            return (T) new BigInteger(s);
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(s);
        } else if (type == Long.class) {
            return (T) Long.valueOf(s);
        } else if (type == Float.class) {
            return (T) Float.valueOf(s);
        } else if (type == Double.class) {
            return (T) Double.valueOf(s);
        } else if (type == Short.class) {
            return (T) Short.valueOf(s);
        } else if (type == Byte.class) {
            return (T) Byte.valueOf(s);
        }
        return null;
    }

    public static String toShortString(BigDecimal value) {
        if (value == null) {
            return null;
        }
        String s = value.toString();
        while (s.contains(Strings.DOT) && s.endsWith("0")) {
            s = s.substring(0, s.length() - 1);
        }
        if (s.endsWith(Strings.DOT)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String getCapacityCaption(long capacity, int scale) {
        String[] units = { "B", "KB", "MB", "GB", "TB", "PB" };
        int unitIndex = 0;
        BigDecimal value = BigDecimal.valueOf(capacity);
        BigDecimal base = BigDecimal.valueOf(1024);
        for (; unitIndex < units.length; unitIndex++) {
            if (value.compareTo(base) >= 0) {
                value = value.divide(base, scale, RoundingMode.HALF_UP);
            } else {
                break;
            }
        }
        return toShortString(value) + units[unitIndex];
    }

    @SuppressWarnings("unchecked")
    public static <T> T toValue(BigDecimal decimal, Class<T> valueType) {
        if (decimal == null || valueType == null) {
            return null;
        }
        if (valueType == BigDecimal.class) {
            return (T) decimal;
        }
        if (valueType == int.class || valueType == Integer.class) {
            return (T) Integer.valueOf(decimal.intValue());
        }
        if (valueType == long.class || valueType == Long.class) {
            return (T) Long.valueOf(decimal.longValue());
        }
        if (valueType == byte.class || valueType == Byte.class) {
            return (T) Byte.valueOf(decimal.byteValue());
        }
        if (valueType == short.class || valueType == Short.class) {
            return (T) Short.valueOf(decimal.shortValue());
        }
        if (valueType == float.class || valueType == Float.class) {
            return (T) Float.valueOf(decimal.floatValue());
        }
        if (valueType == double.class || valueType == Double.class) {
            return (T) Double.valueOf(decimal.doubleValue());
        }
        return null;
    }

    public static String toShortestString(BigDecimal decimal) {
        if (decimal == null) {
            return null;
        }
        String s = decimal.toPlainString();
        if (s.contains(Strings.DOT)) { // 包含小数点才需要缩减
            StringBuilder sb = new StringBuilder(s);
            // 去掉小数点后所有末尾的0
            while (sb.charAt(sb.length() - 1) == '0') {
                sb.deleteCharAt(sb.length() - 1);
            }
            // 如果最后以小数点结尾，则去掉小数点
            if (sb.charAt(sb.length() - 1) == '.') {
                sb.deleteCharAt(sb.length() - 1);
            }
            s = sb.toString();
        }
        return s;
    }

}
