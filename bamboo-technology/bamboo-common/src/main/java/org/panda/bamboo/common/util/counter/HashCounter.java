package org.panda.bamboo.common.util.counter;

import java.util.HashMap;

/**
 * 基于HashMap的计数器
 */
public class HashCounter<K> extends AbstractCounter<K> {

    public HashCounter() {
        super(new HashMap<>());
    }

}
