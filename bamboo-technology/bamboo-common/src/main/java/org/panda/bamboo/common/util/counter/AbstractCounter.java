package org.panda.bamboo.common.util.counter;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 抽象计数器
 */
public abstract class AbstractCounter<K> implements Counter<K> {

    private final Map<K, Long> map;

    protected AbstractCounter(Map<K, Long> map) {
        this.map = map;
    }

    @Override
    public synchronized long add(K key, long step) {
        long count = count(key);
        count += step;
        this.map.put(key, count);
        return count;
    }

    @Override
    public long count(K key) {
        Long count = this.map.get(key);
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Set<Entry<K, Long>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public void fillTo(Map<K, Long> map) {
        map.putAll(this.map);
    }

    @Override
    public Map<K, Long> asMap() {
        return Collections.unmodifiableMap(this.map);
    }

    @Override
    public Iterator<Entry<K, Long>> iterator() {
        return entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Entry<K, Long>> action) {
        entrySet().forEach(action);
    }

    @Override
    public Spliterator<Entry<K, Long>> spliterator() {
        return entrySet().spliterator();
    }

}
