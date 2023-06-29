package org.panda.bamboo.common.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器
 **/
public class AtomicCounter {
    private AtomicInteger counter;

    public AtomicCounter() {
        counter = new AtomicInteger(0);
    }

    public int getCurrentCount() {
        return counter.get();
    }

    public int getNextCount() {
        return counter.incrementAndGet();
    }

    public int getPreviousCount() {
        return counter.decrementAndGet();
    }
}
