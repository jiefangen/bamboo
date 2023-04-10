package org.panda.bamboo.common.util.lang;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 集合工具类
 *
 * @author fangen
 */
public class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * 取指定集合中满足指定断言条件的第一条记录
     *
     * @param iterable  集合
     * @param predicate 断言，为null时忽略
     * @param <T>       记录类型
     * @return 第一条记录
     */
    public static <T> T getFirst(Iterable<T> iterable, Predicate<T> predicate) {
        if (iterable != null) {
            if (predicate == null) {
                Iterator<T> iterator = iterable.iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            } else {
                for (T object : iterable) {
                    if (predicate.test(object)) {
                        return object;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 取指定集合的第一条记录
     *
     * @param iterable 集合
     * @param <T>      记录类型
     * @return 第一条记录
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        return getFirst(iterable, null);
    }

    /**
     * 获取指定可迭代集合中，满足指定断言条件的最后一个元素
     *
     * @param iterable  可迭代集合
     * @param predicate 断言条件，为null时忽略
     * @param <T>       元素类型
     * @return 满足条件的最后一个元素
     */
    public static <T> T getLast(Iterable<T> iterable, Predicate<T> predicate) {
        T result = null;
        if (iterable != null) {
            if (predicate == null) {
                for (T obj : iterable) {
                    result = obj;
                }
            } else {
                for (T obj : iterable) {
                    if (predicate.test(obj)) {
                        result = obj;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取指定可迭代对象的指定索引下标位置处的元素
     *
     * @param iterable 指定可迭代对象，可为null
     * @param index    索引下标，可超出可迭代对象中的元素个数，超出时返回null
     * @param <T>      元素类型
     * @return 元素
     */
    public static <T> T get(Iterable<T> iterable, int index) {
        if (iterable != null && index >= 0) {
            if (iterable instanceof List) {
                List<T> list = (List<T>) iterable;
                if (index < list.size()) {
                    return list.get(index);
                }
            } else {
                if (iterable instanceof Collection) {
                    Collection<T> collection = (Collection<T>) iterable;
                    if (index >= collection.size()) {
                        return null;
                    }
                }
                int i = 0;
                for (T obj : iterable) {
                    if (i++ == index) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取指定集合的大小
     *
     * @param iterable 集合
     * @return 集合的大小
     */
    public static int size(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        } else if (iterable instanceof Map) {
            return ((Map<?, ?>) iterable).size();
        } else {
            int size = 0;
            Iterator<?> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
            return size;
        }
    }

    /**
     * 判断指定集合是否包含指定元素
     *
     * @param iterable 集合
     * @param element  元素
     * @param <T>      元素类型
     * @return 指定集合是否包含指定元素
     */
    public static <T> boolean contains(Iterable<T> iterable, T element) {
        if (iterable != null) {
            for (T e : iterable) {
                if (Objects.equals(e, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的两个集合中的元素是否有一个包含在另一个集合中，两个集合参数满足交换律规则
     *
     * @param iterable1 集合1
     * @param iterable2 集合2
     * @param <T>       元素类型
     * @return 指定的两个集合中的元素是否有一个包含在另一个集合中
     */
    public static <T> boolean containsOneOf(Iterable<T> iterable1, Iterable<T> iterable2) {
        if (iterable1 != null && iterable2 != null) {
            for (T element1 : iterable1) {
                for (T element2 : iterable2) {
                    if (Objects.equals(element1, element2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean equals(Collection<?> collection, Object[] array) {
        if (collection == null && array == null) {
            return true;
        }
        if (collection == null || array == null) {
            return false;
        }
        if (collection.size() != array.length) {
            return false;
        }
        for (Object obj : array) {
            if (!collection.contains(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将指定数据中的所有元素添加到指定集合中
     *
     * @param collection 集合
     * @param array      数组
     * @param <T>        元素类型
     */
    public static <T> void addAll(Collection<T> collection, @Nullable T[] array) {
        if (array != null) {
            Collections.addAll(collection, array);
        }
    }

    /**
     * 将Key为字符串的映射集转为Key为整型的映射集
     *
     * @param map    Key为字符串的映射
     * @param minKey 转换目标整型Key的最小值
     * @return Key为整型的映射集
     */
    public static Map<Integer, String> toIntegerKeyMap(Map<String, String> map, int minKey) {
        Map<Integer, String> newMap = new HashMap<>();
        for (String key : map.keySet()) {
            int newKey = parseInt(key, minKey - 1);
            if (newKey >= minKey) {
                newMap.put(newKey, map.get(key));
            }
        }
        return newMap;
    }

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

    public static Map<String, Integer> toStringKeyMap(Map<Integer, Integer> map) {
        Map<String, Integer> result = new HashMap<>();
        map.forEach((userId, count) -> {
            result.put(userId.toString(), count);
        });
        return result;
    }

    /**
     * 将指定整数对象集合转换为基本整数数组
     *
     * @param collection 集合
     * @return 基本长整数数组
     */
    public static int[] toIntArray(Collection<Integer> collection) {
        if (collection == null) {
            return null;
        }
        int[] array = new int[collection.size()];
        int i = 0;
        for (Integer value : collection) {
            array[i++] = value;
        }
        return array;
    }

    /**
     * 将指定长整数对象集合转换为基本长整数数组
     *
     * @param collection 集合
     * @return 基本长整数数组
     */
    public static long[] toLongArray(Collection<Long> collection) {
        if (collection == null) {
            return null;
        }
        long[] array = new long[collection.size()];
        int i = 0;
        for (Long value : collection) {
            array[i++] = value;
        }
        return array;
    }

    /**
     * 将指定枚举集合转换为key为枚举名称，value为枚举常量的映射集
     *
     * @param collection 枚举集合
     * @param <T>        枚举类型
     * @return 枚举映射集
     */
    public static <T extends Enum<T>> Map<String, T> toMap(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        Map<String, T> map = new HashMap<>();
        for (T constant : collection) {
            map.put(constant.name(), constant);
        }
        return map;
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        if (iterable instanceof List) {
            return (List<T>) iterable;
        } else {
            List<T> list = new ArrayList<>();
            iterable.forEach(list::add);
            return list;
        }
    }

    public static List<Integer> toList(int... array) {
        if (array == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        for (int e : array) {
            list.add(e);
        }
        return list;
    }

    public static List<Long> toList(long... array) {
        if (array == null) {
            return null;
        }
        List<Long> list = new ArrayList<>();
        for (long e : array) {
            list.add(e);
        }
        return list;
    }

    public static List<Object> toList(Object... array) {
        if (array == null) {
            return null;
        }
        List<Object> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    public static <K, V> Map<K, V> clone(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        try {
            Map<K, V> result = map.getClass().getConstructor().newInstance();
            result.putAll(map);
            return result;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Collection<T> clone(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        try {
            Collection<T> result = collection.getClass().getConstructor().newInstance();
            result.addAll(collection);
            return result;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从迭代器中移除符合指定断言的元素
     *
     * @param iterator  迭代器
     * @param predicate 移除断言
     * @param <T>       元素类型
     */
    public static <T> void remove(Iterator<T> iterator, Predicate<T> predicate) {
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                iterator.remove();
            }
        }
    }

    /**
     * 从迭代器中移除符合指定断言的元素
     *
     * @param iterator  迭代器
     * @param predicate 移除断言
     * @param <T>       元素类型
     */
    public static <T> void remove(Iterator<T> iterator, BiPredicate<T, Integer> predicate) {
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next(), i++)) {
                iterator.remove();
            }
        }
    }

    /**
     * 从迭代集合中移除符合指定断言的元素
     *
     * @param iterable  迭代集合
     * @param predicate 移除断言
     * @param <T>       元素类型
     */
    public static <T> void remove(Iterable<T> iterable, Predicate<T> predicate) {
        Iterator<T> iterator = iterable.iterator();
        remove(iterator, predicate);
    }

    /**
     * 从迭代集合中移除符合指定断言的元素
     *
     * @param iterable  迭代集合
     * @param predicate 移除断言
     * @param <T>       元素类型
     */
    public static <T> void remove(Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        Iterator<T> iterator = iterable.iterator();
        remove(iterator, predicate);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

    public static <T> void reset(Collection<T> source, Collection<T> target) {
        target.clear();
        if (source != null) {
            target.addAll(source);
        }
    }

    public static <K, V> void reset(Map<K, V> source, Map<K, V> target) {
        target.clear();
        if (source != null) {
            target.putAll(source);
        }
    }

    /**
     * 将指定映射集转换为按值排序的映射集返回，指定映射集没有变化
     *
     * @param map 映射集
     * @param <K> 键类型
     * @param <V> 值类型
     * @return 按值排序的映射集
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortedByValueMap(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        Map<K, V> result = new LinkedHashMap<>();
        List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        entryList.forEach(entry -> {
            result.put(entry.getKey(), entry.getValue());
        });
        return result;
    }

    /**
     * 将指定映射集转换为按值排序的映射集返回，指定映射集没有变化
     *
     * @param map        映射集
     * @param comparator 排序比较器
     * @param <K>        键类型
     * @param <V>        值类型
     * @return 按值排序的映射集
     */
    public static <K, V> Map<K, V> sortedByValueMap(Map<K, V> map, Comparator<V> comparator) {
        if (map == null || comparator == null) {
            return map;
        }
        Map<K, V> result = new LinkedHashMap<>();
        List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort((o1, o2) -> comparator.compare(o1.getValue(), o2.getValue()));
        entryList.forEach(entry -> {
            result.put(entry.getKey(), entry.getValue());
        });
        return result;
    }

    public static <E> int indexOf(List<E> list, Predicate<E> predicate) {
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 逐一转换指定清单中的元素组成新的清单
     *
     * @param list   转换前的清单
     * @param mapper 转换函数，返回null将不纳入结果清单中
     * @param <T>    来源元素类型
     * @param <R>    结果元素类型
     * @return 转换后的清单
     */
    public static <T, R> List<R> map(List<T> list, Function<? super T, ? extends R> mapper) {
        if (list != null) {
            List<R> result = new ArrayList<>();
            for (T t : list) {
                R r = mapper.apply(t);
                if (r != null) {
                    result.add(r);
                }
            }
            return result;
        }
        return null;
    }

    public static Map<String, String> toStringMap(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>();
        map.forEach((key, value) -> result.put(key, value.toString()));
        return result;
    }

}
