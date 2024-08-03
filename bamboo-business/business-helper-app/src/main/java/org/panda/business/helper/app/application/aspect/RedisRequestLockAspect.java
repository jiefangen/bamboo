package org.panda.business.helper.app.application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.panda.tech.core.spec.debounce.annotation.RequestLock;
import org.panda.tech.core.spec.debounce.support.RequestLockSupport;
import org.panda.tech.data.redis.lock.RedisCacheLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存请求锁切面
 *
 * @author fangen
 **/
@Aspect
@Configuration
public class RedisRequestLockAspect extends RequestLockSupport {

    private final RedisCacheLock redisCacheLock;

    @Autowired
    public RedisRequestLockAspect(RedisCacheLock redisCacheLock) {
        this.redisCacheLock = redisCacheLock;
    }

    @Around("execution(public * * (..)) && @annotation(org.panda.tech.core.spec.debounce.annotation.RequestLock)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        return super.doAround(joinPoint);
    }

    @Override
    protected Boolean isLocked(String lockKey, RequestLock requestLock) {
        return redisCacheLock.isLocked(lockKey, requestLock.expire(), requestLock.timeUnit());
    }
}
