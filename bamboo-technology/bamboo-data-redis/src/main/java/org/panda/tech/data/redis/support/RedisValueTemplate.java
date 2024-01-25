package org.panda.tech.data.redis.support;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.clazz.BeanUtil;
import org.panda.bamboo.common.util.lang.StringUtil;
import org.panda.tech.data.model.query.FieldOrder;
import org.panda.tech.data.model.query.QueryResult;
import org.panda.tech.data.model.query.spec.QueryIgnoring;
import org.panda.tech.data.model.query.spec.Querying;
import org.panda.tech.data.redis.serializer.BaseTypeJackson2JsonRedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.function.*;

public class RedisValueTemplate<K, T> {

    private final RedisOperations<String, T> operations;
    private final Supplier<String> keyPrefixSupplier;
    private final Class<K> keyClass;
    private BiFunction<String, Class<K>, K> keyParser = StringUtil::parse;

    @SuppressWarnings("unchecked")
    public RedisValueTemplate(RedisConnectionFactory connectionFactory, Class<K> keyClass, Class<T> valueClass,
            Supplier<String> keyPrefixSupplier) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        RedisSerializer<T> valueSerializer;
        if (valueClass == String.class) {
            valueSerializer = (RedisSerializer<T>) StringRedisSerializer.UTF_8;
        } else {
            valueSerializer = new BaseTypeJackson2JsonRedisSerializer<>(valueClass);
        }
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        this.operations = redisTemplate;
        this.keyClass = keyClass;
        this.keyPrefixSupplier = keyPrefixSupplier;
    }

    public RedisValueTemplate(RedisConnectionFactory connectionFactory, Class<K> keyClass, Class<T> valueClass,
            String keyPrefix) {
        this(connectionFactory, keyClass, valueClass, () -> keyPrefix);
    }

    public boolean isConnectable() {
        try {
            this.operations.execute(RedisConnectionCommands::ping);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setKeyParser(BiFunction<String, Class<K>, K> keyParser) {
        this.keyParser = keyParser;
    }

    public String getKeyPrefix() {
        String keyPrefix = this.keyPrefixSupplier.get();
        return keyPrefix == null ? Strings.EMPTY : keyPrefix;
    }

    public RedisOperations<String, T> getOperations() {
        return this.operations;
    }

    public ValueOperations<String, T> ops() {
        return this.operations.opsForValue();
    }

    private Set<String> getInternalKeys(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = Strings.ASTERISK;
        }
        pattern = getKeyPrefix() + pattern;
        Set<String> keys = this.operations.keys(pattern);
        if (keys == null) {
            keys = Collections.emptySet();
        }
        return keys;
    }

    public Set<K> getKeys(String pattern) {
        Set<K> keys = new LinkedHashSet<>();
        int keyPrefixLength = getKeyPrefix().length();
        Set<String> internalKeys = getInternalKeys(pattern);
        for (String internalKey : internalKeys) {
            String keyValue = internalKey.substring(keyPrefixLength);
            K key = this.keyParser.apply(keyValue, this.keyClass);
            keys.add(key);
        }
        return keys;
    }

    public boolean existsKeys(String pattern) {
        Set<String> internalKeys = getInternalKeys(pattern);
        return internalKeys.size() > 0;
    }

    private String getInternalKey(K key) {
        return getKeyPrefix() + (key == null ? Strings.EMPTY : key);
    }

    public boolean hasKey(K key) {
        String internalKey = getInternalKey(key);
        return this.operations.hasKey(internalKey) == Boolean.TRUE;
    }

    public T delete(K key) {
        String internalKey = getInternalKey(key);
        T value = ops().get(internalKey);
        if (value != null) {
            this.operations.delete(internalKey);
        }
        return value;
    }

    public void deleteAll() {
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            this.operations.delete(internalKey);
        }
    }

    public T get(K key) {
        String internalKey = getInternalKey(key);
        return ops().get(internalKey);
    }

    public void set(K key, T value) {
        String internalKey = getInternalKey(key);
        ops().set(internalKey, value);
    }

    public long increment(K key, long delta) {
        String internalKey = getInternalKey(key);
        Long value = ops().increment(internalKey, delta);
        return value == null ? 0 : value;
    }

    public long increment(K key) {
        String internalKey = getInternalKey(key);
        Long value = ops().increment(internalKey);
        return value == null ? 0 : value;
    }

    public void forEach(BiConsumer<String, T> consumer) {
        Set<String> internalKeys = getInternalKeys(null);
        int keyPrefixLength = getKeyPrefix().length();
        for (String internalKey : internalKeys) {
            String key = internalKey.substring(keyPrefixLength);
            T value = ops().get(internalKey);
            consumer.accept(key, value);
        }
    }

    /**
     * 遍历每一个值给指定消费者
     *
     * @param consumer 消费者
     */
    public void forEach(Consumer<T> consumer) {
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            consumer.accept(value);
        }
    }

    /**
     * 遍历每一个值给指定断言者，直到断言者检查失败
     *
     * @param predicate 断言者
     */
    public void forEach(Predicate<T> predicate) {
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (!predicate.test(value)) {
                return;
            }
        }
    }

    /**
     * 遍历每一个值给指定二元断言者，直到断言者检查失败
     *
     * @param predicate 断言者
     */
    public void forEach(BiPredicate<T, Integer> predicate) {
        int index = 0;
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (!predicate.test(value, index++)) {
                return;
            }
        }
    }

    public List<T> list(Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (value != null && predicate.test(value)) {
                list.add(value);
            }
        }
        return list;
    }

    public T first(Predicate<T> predicate) {
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (predicate.test(value)) {
                return value;
            }
        }
        return null;
    }

    public boolean exists(Predicate<T> predicate) {
        Set<String> internalKeys = getInternalKeys(null);
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (predicate.test(value)) {
                return true;
            }
        }
        return false;
    }

    public long count(String keyPattern, Predicate<T> predicate) {
        Set<String> internalKeys = getInternalKeys(keyPattern);
        if (predicate == null) {
            return internalKeys.size();
        }
        long total = 0;
        for (String internalKey : internalKeys) {
            T value = ops().get(internalKey);
            if (predicate.test(value)) {
                total += 1;
            }
        }
        return total;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public QueryResult<T> query(Querying querying, Predicate<T> predicate) {
        int pageSize = querying.getPageSize();
        if (pageSize < 1) {
            pageSize = 20;
        }
        int pageNo = querying.getPageNo();
        if (pageNo < 1) {
            pageNo = 1;
        }
        int beginIndex = pageSize * (pageNo - 1);
        int endIndex = beginIndex + pageSize;
        List<FieldOrder> orders = querying.getOrders();
        // 需要获取总数或需要排序，则需要获取所有的值进行判断
        boolean allValueRequired = querying.getIgnoring() != QueryIgnoring.TOTAL || CollectionUtils.isNotEmpty(orders);

        List<T> list = new ArrayList<>();
        if (allValueRequired) {
            forEach(value -> {
                if (predicate.test(value)) {
                    list.add(value);
                }
            });
            if (CollectionUtils.isNotEmpty(orders)) {
                list.sort((v1, v2) -> {
                    for (FieldOrder order : orders) {
                        String fieldName = order.getName();
                        Object fieldValue1 = BeanUtil.getPropertyValue(v1, fieldName);
                        Object fieldValue2 = BeanUtil.getPropertyValue(v2, fieldName);
                        if (order.isDesc()) { // 反序排列，则对换取值
                            Object tempValue = fieldValue1;
                            fieldValue1 = fieldValue2;
                            fieldValue2 = tempValue;
                        }
                        int compared = Comparator.nullsFirst((fv1, fv2) -> {
                            if (fv1 instanceof String) { // 字符串忽略大小写进行比较
                                return ((String) fv1).toLowerCase().compareTo(fv2.toString().toLowerCase());
                            } else if (Comparable.class.isAssignableFrom(fv1.getClass())) {
                                return ((Comparable) fv1).compareTo(fv2);
                            }
                            return 0;
                        }).compare(fieldValue1, fieldValue2);
                        if (compared != 0) {
                            return compared;
                        }
                    }
                    return 0;
                });
            }
            int total = list.size();
            List<T> records = list.subList(beginIndex, Math.min(endIndex, total));
            return QueryResult.of(records, pageSize, pageNo, (long) total, orders);
        } else { // 如果不获取所有的值，则只需取到分页最后索引位置即可
            forEach(value -> {
                if (list.size() > endIndex) { // 多取一条，以判断后续还有没有更多
                    return false;
                }
                if (predicate.test(value)) {
                    list.add(value);
                }
                return true;
            });
            return QueryResult.of(list, pageSize, pageNo, null, null);
        }
    }

}
