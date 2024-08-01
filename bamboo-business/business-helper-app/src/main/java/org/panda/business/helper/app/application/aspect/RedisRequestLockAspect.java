package org.panda.business.helper.app.application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.panda.tech.core.spec.debounce.annotation.RequestLock;
import org.panda.tech.core.spec.debounce.support.RequestLockSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

/**
 * 缓存请求锁切面
 *
 * @author fangen
 **/
@Aspect
@Configuration
public class RedisRequestLockAspect extends RequestLockSupport {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisRequestLockAspect(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Around("execution(public * * (..)) && @annotation(org.panda.tech.core.spec.debounce.annotation.RequestLock)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        return super.doAround(joinPoint);
    }

    @Override
    protected Boolean isLocked(String lockKey, RequestLock requestLock) {
        // 使用RedisCallback接口执行set命令，设置锁键；设置额外选项：过期时间和SET_IF_ABSENT选项
        return stringRedisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(), new byte[0],
                        Expiration.from(requestLock.expire(), requestLock.timeUnit()),
                        RedisStringCommands.SetOption.SET_IF_ABSENT));
    }
}
