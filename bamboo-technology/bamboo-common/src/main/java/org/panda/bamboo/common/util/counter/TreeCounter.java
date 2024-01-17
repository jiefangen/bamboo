package org.panda.bamboo.common.util.counter;

import java.util.TreeMap;

/**
 * 基于TreeMap的计数器
 */
public class TreeCounter<K> extends AbstractCounter<K> {

    public TreeCounter() {
        super(new TreeMap<>());
    }

}
