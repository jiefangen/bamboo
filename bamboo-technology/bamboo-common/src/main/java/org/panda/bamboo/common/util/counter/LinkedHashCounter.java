package org.panda.bamboo.common.util.counter;

import java.util.LinkedHashMap;

/**
 * 基于LinkedHashMap的计数器
 */
public class LinkedHashCounter<K> extends AbstractCounter<K> {

    public LinkedHashCounter() {
        super(new LinkedHashMap<>());
    }

}
