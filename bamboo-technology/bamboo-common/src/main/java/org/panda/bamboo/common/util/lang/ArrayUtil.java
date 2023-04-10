package org.panda.bamboo.common.util.lang;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 数组工具类
 *
 * @author fangen
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * 取指定数组中满足指定断言条件的第一条记录
     *
     * @param array     数组
     * @param predicate 断言，为null时忽略
     * @param <T>       记录类型
     * @return 第一条记录
     */
    public static <T> T getFirst(T[] array, Predicate<T> predicate) {
        if (array != null && array.length > 0) {
            if (predicate == null) {
                return array[0];
            } else {
                for (T object : array) {
                    if (predicate.test(object)) {
                        return object;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取指定数组中，满足指定断言条件的最后一个元素
     *
     * @param array     数组
     * @param predicate 断言条件，为null时忽略
     * @param <T>       元素类型
     * @return 满足条件的最后一个元素
     */
    public static <T> T getLast(T[] array, Predicate<T> predicate) {
        T result = null;
        if (array != null) {
            if (predicate == null) {
                for (T obj : array) {
                    result = obj;
                }
            } else {
                for (T obj : array) {
                    if (predicate.test(obj)) {
                        result = obj;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 从指定对象数组中获取指定索引下标处的对象，如果指定数组为空或长度不够，则返回null
     *
     * @param array 对象数组
     * @param index 索引下标
     * @param <T>   对象类型
     * @return 指定索引下标处的对象
     */
    public static <T> T get(T[] array, int index) {
        if (array == null || index < 0 || array.length <= index) {
            return null;
        }
        return array[index];
    }

    /**
     * 从指定整型数组中获取指定索引下标处的整数，如果指定数组为空或长度不够，则返回指定默认值
     *
     * @param array        整型数组
     * @param index        索引下标
     * @param defaultValue 默认值
     * @return 指定索引下标处的整数值
     */
    public static int get(int[] array, int index, int defaultValue) {
        if (array == null || index < 0 || array.length <= index) {
            return defaultValue;
        }
        return array[index];
    }

    /**
     * 从指定长整型数组中获取指定索引下标处的长整数，如果指定数组为空或长度不够，则返回指定默认值
     *
     * @param array        长整型数组
     * @param index        索引下标
     * @param defaultValue 默认值
     * @return 指定索引下标处的长整数值
     */
    public static long get(long[] array, int index, long defaultValue) {
        if (array == null || index < 0 || array.length <= index) {
            return defaultValue;
        }
        return array[index];
    }

    /**
     * 将string数组转换成int数组
     *
     * @param stringArray 字符串数组
     * @return int数组
     */
    public static int[] toIntArray(String[] stringArray) {
        if (stringArray == null) {
            return null;
        }
        int length = stringArray.length;
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = MathUtil.parseInt(stringArray[i]);
        }
        return intArray;
    }

    public static int[] toIntArray(Integer[] integerArray) {
        if (integerArray == null) {
            return null;
        }
        int length = integerArray.length;
        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            intArray[i] = integerArray[i];
        }
        return intArray;
    }

    public static Integer[] toIntegerArray(int[] intArray) {
        if (intArray == null) {
            return null;
        }
        int length = intArray.length;
        Integer[] integerArray = new Integer[length];
        for (int i = 0; i < length; i++) {
            integerArray[i] = intArray[i];
        }
        return integerArray;
    }

    public static Long[] toLongObjectArray(long[] longArray) {
        if (longArray == null) {
            return null;
        }
        int length = longArray.length;
        Long[] result = new Long[length];
        for (int i = 0; i < length; i++) {
            result[i] = longArray[i];
        }
        return result;
    }

    public static long[] toLongValueArray(Long[] longArray) {
        if (longArray == null) {
            return null;
        }
        int length = longArray.length;
        long[] result = new long[length];
        for (int i = 0; i < length; i++) {
            result[i] = longArray[i];
        }
        return result;
    }

    public static long[] toLongValueArray(String[] stringArray) {
        if (stringArray == null) {
            return null;
        }
        int length = stringArray.length;
        long[] longArray = new long[length];
        for (int i = 0; i < length; i++) {
            longArray[i] = MathUtil.parseLong(stringArray[i]);
        }
        return longArray;
    }

    /**
     * 将指定变参数组转换为List，与{@link Arrays#asList(Object...)}的区别是：本方法返回的List还可以进一步改动
     *
     * @param array 变参数组
     * @param <T>   元素类型
     * @return List
     */
    @SafeVarargs
    public static <T> List<T> asList(T... array) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    @SafeVarargs
    public static <T> Set<T> asSet(T... array) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, array);
        return set;
    }

    public static Set<Integer> toSet(int[] array) {
        if (array == null) {
            return null;
        }
        Set<Integer> set = new HashSet<>();
        for (int i : array) {
            set.add(i);
        }
        return set;
    }

    public static Set<Long> toSet(long[] array) {
        if (array == null) {
            return null;
        }
        Set<Long> set = new HashSet<>();
        for (long l : array) {
            set.add(l);
        }
        return set;
    }

    public static <T> Set<T> toSet(T[] array) {
        if (array == null) {
            return null;
        }
        return asSet(array);
    }

    public static List<Integer> toList(int[] array) {
        if (array == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }

    public static List<Long> toList(long[] array) {
        if (array == null) {
            return null;
        }
        List<Long> list = new ArrayList<>();
        for (long i : array) {
            list.add(i);
        }
        return list;
    }

    public static <T> List<T> toList(T[] array) {
        if (array == null) {
            return null;
        }
        return asList(array);
    }

    public static void toLowerCase(String[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    array[i] = array[i].toLowerCase();
                }
            }
        }
    }

    public static void toUpperCase(String[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    array[i] = array[i].toUpperCase();
                }
            }
        }
    }

    public static void trim(String[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    array[i] = array[i].trim();
                }
            }
        }
    }

    public static boolean containsIgnoreCase(String[] array, String value) {
        for (String e : array) {
            if (e == null) {
                if (value == null) {
                    return true;
                }
            } else if (e.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] removeIf(T[] array, BiPredicate<T, Integer> predicate) {
        if (ArrayUtils.isNotEmpty(array)) {
            List<T> list = new ArrayList<>();
            Class<T> componentType = null;
            for (int i = 0; i < array.length; i++) {
                T t = array[i];
                componentType = (Class<T>) t.getClass();
                if (!predicate.test(t, i)) {
                    list.add(t);
                }
            }
            if (list.size() < array.length) {
                return list.toArray((T[]) Array.newInstance(componentType, 0));
            }
        }
        return array;
    }

    /**
     * 移除指定数组中的null值，去重后，进行排序
     *
     * @param array 数组
     * @param <T>   可排序的元素类型
     * @return 处理后得到的集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Comparable> List<T> sortValidly(T[] array) {
        Set<T> set = new HashSet<>();
        for (T obj : array) {
            if (obj != null) {
                set.add(obj);
            }
        }
        List<T> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }

    public static boolean deepEquals(byte[] bytes, short[] shorts) {
        if (ArrayUtils.isEmpty(bytes) && ArrayUtils.isEmpty(shorts)) {
            return true;
        }
        if (bytes.length != shorts.length) {
            return false;
        }
        for (int i = 0; i < bytes.length; i++) {
            if (((short) bytes[i]) != shorts[i]) {
                return false;
            }
        }
        return true;
    }

}
