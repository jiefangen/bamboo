package org.panda.tech.data.redis.template;

import org.panda.bamboo.core.beans.ContextInitializedBean;
import org.panda.bamboo.core.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

abstract class CacheTemplate<T> implements ContextInitializedBean {

    private RedisTemplate<String, T> redisTemplate;

    protected CacheTemplate() {
    }

    public CacheTemplate(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected abstract String getRedisBeanName();

    @Override
    public void afterInitialized(ApplicationContext context) throws Exception {
        this.redisTemplate = SpringUtil.getBeanByName(context, getRedisBeanName());
    }

    /** -------------------key相关操作--------------------- */

    /**
     * 删除 key
     *
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除 key
     *
     * @param keys 键集合
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 序列化 key
     *
     * @param key 键
     * @return byte[]
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在 key
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param timeout 失效时长
     * @param unit 时间单位
     * @return Boolean
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间，单位秒
     *
     * @param key 键
     * @param timeout 失效时长
     * @return Boolean
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key 键
     * @param date 过期时间
     * @return Boolean
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的 key
     *
     * @param pattern 匹配模式
     * @return Set<String>
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库db当中
     *
     * @param key 键
     * @param dbIndex 数据库序号
     * @return Boolean
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 键
     * @param unit 时间单位
     * @return Long
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 键
     * @return Long
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return String
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧键
     * @param newKey 新键
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 旧键
     * @param newKey 新键
     * @return Boolean
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 键
     * @return DataType
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /** -------------------string相关操作--------------------- */

    @SuppressWarnings("unchecked")
    public <V> ValueOperations<String, V> ops() {
        return (ValueOperations<String, V>) redisTemplate.opsForValue();
    }

    /**
     * 设置指定 key 的值
     *
     * @param key 键
     * @param value 值
     * @param <V> 值类型
     */
    public <V> void set(String key, V value) {
        ops().set(key, value);
    }

    /**
     * 设置指定 key 的值
     *
     * @param key 键
     * @param value 值
     * @param seconds 失效时间，秒
     * @param <V> 值类型
     */
    public <V> void set(String key, V value, Integer seconds) {
        ops().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @param <V> 值类型
     */
    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        return (V) ops().get(key);
    }

    /**
     * 获取 key 中字符串值的子字符
     *
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return String
     */
    public String getRange(String key, long start, long end) {
        return ops().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     *
     * @param key 键
     * @param value 值
     * @return V
     */
    public <V> V getAndSet(String key, V value) {
        ValueOperations<String, V> opeForValue = (ValueOperations<String, V>) redisTemplate.opsForValue();
        return opeForValue.getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key 键
     * @param offset 偏移量
     * @return Boolean
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 批量获取
     *
     * @param keys 键集合
     * @return List<V>
     */
    public <V> List<V> multiGet(Collection<String> keys) {
        ValueOperations<String, V> opeForValue = (ValueOperations<String, V>) redisTemplate.opsForValue();
        return opeForValue.multiGet(keys);
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
     *
     * @param key 键
     * @param offset 位置
     * @param value 值：true为1, false为0
     * @return Boolean
     */
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key 键
     * @param value 值
     * @param timeout 失效时长
     * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public <V> void setEx(String key, V value, long timeout, TimeUnit unit) {
        ops().set(key, value, timeout, unit);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key 键
     * @param value 值
     * @return 之前已经存在返回false, 不存在返回true
     */
    public <V> Boolean setIfAbsent(String key, V value) {
        return ops().setIfAbsent(key, value);
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key 键
     * @param value 值
     * @param offset 从指定位置开始覆写
     */
    public <V> void setRange(String key, V value, long offset) {
        ops().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key 键
     * @return Long
     */
    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 批量添加
     *
     * @param maps 新增集合
     */
    public <V> void multiSet(Map<String, V> maps) {
        ops().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param maps 集合
     * @return 之前已经存在返回false, 不存在返回true
     */
    public <V> Boolean multiSetIfAbsent(Map<String, V> maps) {
        return ops().multiSetIfAbsent(maps);
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key 键
     * @param increment 增量值
     * @return Long
     */
    public Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @param key 键
     * @param increment 增量值
     * @return Double
     */
    public Double incrByFloat(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 追加到末尾
     *
     * @param key 键
     * @param value 值
     * @return Integer
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /** -------------------hash相关操作------------------------- */

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key 键
     * @param field 属性
     * @return V
     */
    public <K, V> V hGet(String key, String field) {
        HashOperations<String, K, V> opeForHash = redisTemplate.opsForHash();
        return opeForHash.get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 键
     * @return Map<K, V>
     */
    public <K,V> Map<K, V> hGetAll(String key) {
        HashOperations<String, K, V> opeForHash = redisTemplate.opsForHash();
        return opeForHash.entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 键
     * @param fields 属性集合
     * @return List<V>
     */
    public <K,V> List<V> hMultiGet(String key, Collection<K> fields) {
        HashOperations<String, K, V> opeForHash = redisTemplate.opsForHash();
        return opeForHash.multiGet(key, fields);
    }

    public <K,V> void hPut(String key, K hashKey, V value) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
    }

    public <K,V> void hPutAll(String key, Map<K, V> maps) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, maps);
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key 键
     * @param hashKey 哈希键
     * @param value 值
     * @return Boolean
     */
    public <K,V> Boolean hPutIfAbsent(String key, K hashKey, V value) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key 键
     * @param fields 属性集
     * @return Long
     */
    public <K,V> Long hDelete(String key, K... fields) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.delete(key, (Object) fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key 键
     * @param field 属性
     * @return boolean
     */
    public <K,V> boolean hExists(String key, K field) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     */
    public <K,V> Long hIncrBy(String key, K field, long increment) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     */
    public <K,V> Double hIncrByFloat(String key, K field, double delta) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     */
    public <K,V> Set<K> hKeys(String key) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     */
    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取哈希表中所有值
     */
    public <K,V> List<V> hValues(String key) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(key);
    }

    /**
     * 迭代哈希表中的键值对
     */
    public <K,V> Cursor<Map.Entry<K, V>> hScan(String key, ScanOptions options) {
        HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
        return hashOperations.scan(key, options);
    }

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param key 键
     * @param index 索引位置
     */
    public <V> V lIndex(String key, long index) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key 键
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     */
    public <V> List<V> lRange(String key, long start, long end) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.range(key, start, end);
    }

    /**
     * 存储在list头部
     *
     * @param key 键
     * @param value 值
     */
    public <V> Long lLeftPush(String key, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPush(key, value);
    }

    public <V> Long lLeftPushAll(String key, V... value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPushAll(key, value);
    }

    public <V> Long lLeftPushAll(String key, Collection<V> value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key 键
     * @param value 值
     */
    public <V> Long lLeftPushIfPresent(String key, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     *
     * @param key 键
     * @param pivot 参考位置
     * @param value 值
     */
    public <V> Long lLeftPush(String key, V pivot, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPush(key, pivot, value);
    }

    public <V> Long lRightPush(String key, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPush(key, value);
    }

    public <V> Long lRightPushAll(String key, V... value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPushAll(key, value);
    }

    public <V> Long lRightPushAll(String key, Collection<V> value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPushAll(key, value);
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key 键
     * @param value 值
     */
    public <V> Long lRightPushIfPresent(String key, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     *
     * @param key 键
     * @param pivot 参考位置
     * @param value 值
     */
    public <V> Long lRightPush(String key, V pivot, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key 键
     * @param index 位置
     * @param value 值
     */
    public <V> void lSet(String key, long index, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        listOperations.set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key 键
     * @return 删除的元素
     */
    public <V> V lLeftPop(String key) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key 键
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public <V> V lBLeftPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key 键
     * @return 删除的元素
     */
    public <V> V lRightPop(String key) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key 键
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public <V> V lBRightPop(String key, long timeout, TimeUnit unit) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey 源键
     * @param destinationKey 目标键
     */
    public <V> V lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPopAndLeftPush(sourceKey,
                destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey 源键
     * @param destinationKey 目标键
     * @param timeout 失效时长
     * @param unit 单位
     */
    public <V> V lBRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.rightPopAndLeftPush(sourceKey,
                destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key 键
     * @param index index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *              index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value 值
     */
    public <V> Long lRemove(String key, long index, V value) {
        ListOperations<String,V> listOperations = (ListOperations<String, V>) redisTemplate.opsForList();
        return listOperations.remove(key, index, value);
    }

    /**
     * 裁剪list
     *
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     */
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /** --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param key 键
     * @param values 值集合
     */
    public <V> Long sAdd(String key, V... values) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key 键
     * @param values 值集合
     */
    public <V> Long sRemove(String key, V... values) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 键
     */
    public <V> V sPop(String key) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key 键
     * @param value 值
     * @param destKey 目标集合键
     */
    public <V> Boolean sMove(String key, V value, String destKey) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key 键
     * @param value 值
     */
    public <V> Boolean sIsMember(String key, V value) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key 键
     * @param otherKey 交集键
     */
    public <V> Set<V> sIntersect(String key, String otherKey) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key 键
     * @param otherKeys 交集集合键
     */
    public <V> Set<V> sIntersect(String key, Collection<String> otherKeys) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                          String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取两个集合的并集
     */
    public <V> Set<V> sUnion(String key, String otherKeys) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     */
    public <V> Set<V> sUnion(String key, Collection<String> otherKeys) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     */
    public <V> Set<V> sDifference(String key, String otherKey) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     */
    public <V> Set<V> sDifference(String key, Collection<String> otherKeys) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     */
    public Long sDifference(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取集合所有元素
     */
    public <V> Set<V> sMembers(String key) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    /**
     * 随机获取集合中的一个元素
     */
    public <V> V sRandomMember(String key) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     */
    public <V> List<V> sRandomMembers(String key, long count) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     */
    public <V> Set<V> sDistinctRandomMembers(String key, long count) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.distinctRandomMembers(key, count);
    }

    public <V> Cursor<V> sScan(String key, ScanOptions options) {
        SetOperations<String,V> setOperations = (SetOperations<String, V>) redisTemplate.opsForSet();
        return setOperations.scan(key, options);
    }

    /**------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     */
    public <V> Boolean zAdd(String key, V value, double score) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.add(key, value, score);
    }

    public <V> Long zAdd(String key, Set<ZSetOperations.TypedTuple<V>> values) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.add(key, values);
    }

    public <V> Long zRemove(String key, V... values) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     */
    public <V> Double zIncrementScore(String key, V value, double delta) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key 键
     * @param value 值
     * @return 0表示第一位
     */
    public <V> Long zRank(String key, V value) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     */
    public <V> Long zReverseRank(String key, V value) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key 键
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public <V> Set<V> zRange(String key, long start, long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     */
    public <V> Set<ZSetOperations.TypedTuple<V>> zRangeWithScores(String key, long start, long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public <V> Set<V> zRangeByScore(String key, double min, double max) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public <V> Set<ZSetOperations.TypedTuple<V>> zRangeByScoreWithScores(String key,
                                                                                double min, double max) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.rangeByScoreWithScores(key, min, max);
    }

    public <V> Set<ZSetOperations.TypedTuple<V>> zRangeByScoreWithScores(String key, double min, double max, long start,
                                                                         long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.rangeByScoreWithScores(key, min, max,
                start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     */
    public <V> Set<V> zReverseRange(String key, long start, long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     */
    public <V> Set<ZSetOperations.TypedTuple<V>> zReverseRangeWithScores(String key, long start, long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRangeWithScores(key, start,
                end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     */
    public <V> Set<V> zReverseRangeByScore(String key, double min, double max) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     */
    public <V> Set<ZSetOperations.TypedTuple<V>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRangeByScoreWithScores(key, min, max);
    }

    public <V>  Set<V> zReverseRangeByScore(String key, double min, double max, long start, long end) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 根据score值获取集合元素数量
     */
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取集合大小
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取集合大小
     */
    public Long zZCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取集合中value元素的score值
     */
    public <V> Double zScore(String key, V value) {
        ZSetOperations<String,V> zsetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zsetOperations.score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     */
    public Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    public Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取交集
     */
    public Long zIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 获取交集
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    public <V> Cursor<ZSetOperations.TypedTuple<V>> zScan(String key, ScanOptions options) {
        ZSetOperations<String,V> zSetOperations = (ZSetOperations<String, V>) redisTemplate.opsForZSet();
        return zSetOperations.scan(key, options);
    }
}
