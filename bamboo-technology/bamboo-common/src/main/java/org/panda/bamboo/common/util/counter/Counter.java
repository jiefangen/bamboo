package org.panda.bamboo.common.util.counter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 计数器
 */
public interface Counter<K> extends Iterable<Entry<K, Long>> {

    long add(K key, long step);

    long count(K key);

    int size();

    boolean isEmpty();

    Set<K> keySet();

    Set<Entry<K, Long>> entrySet();

    void fillTo(Map<K, Long> map);

    Map<K, Long> asMap();

}
