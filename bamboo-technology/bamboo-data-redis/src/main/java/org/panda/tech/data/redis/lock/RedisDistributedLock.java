package org.panda.tech.data.redis.lock;

import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.data.redis.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 基于Redis的分布式锁
 * 适用于单节点或主从复制环境
 **/
public class RedisDistributedLock {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 阻塞式获取锁
     */
    public void lock(String lockKey) {
        Lock lock = obtainLock(lockKey);
        lock.lock();
    }

    /**
     * 非阻塞式获取锁
     * 立即得到结果
     */
    public boolean tryLock(String lockKey) {
        Lock lock = obtainLock(lockKey);
        return lock.tryLock();
    }

    /**
     * 非阻塞式获取锁，带有超时时间
     * 在指定时间内获取锁
     */
    public boolean tryLock(String lockKey, long seconds) {
        Lock lock = obtainLock(lockKey);
        try {
            return lock.tryLock(seconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放并清理过期的锁
     */
    public void unlock(String lockKey) {
        try {
            Lock lock = obtainLock(lockKey);
            lock.unlock();
            redisLockRegistry.expireUnusedOlderThan(RedisConstants.DEFAULT_EXPIRE_UNUSED);
        } catch (Exception e) {
            LogUtil.error(getClass(), "Distributed lock [{}] release exception {}", lockKey, e);
        }
    }

    private Lock obtainLock(String lockKey) {
        return redisLockRegistry.obtain(lockKey);
    }
}
