package org.panda.business.official.modules.home;

import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.bamboo.common.util.date.DateUtil;
import org.panda.business.official.infrastructure.cache.RedisCacheService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.repository.cache.SysUserCacheRepo;
import org.panda.tech.core.web.restful.RestfulResult;
import org.panda.tech.data.redis.lock.RedisDistributedLock;
import org.panda.tech.security.config.annotation.ConfigAnonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Api(tags = "缓存数据控制器")
@RestController
@RequestMapping("/redis")
public class RedisCacheController {

    @Autowired
    private RedisCacheService redisCacheService;
    @Resource
    private SysUserCacheRepo sysUserCacheRepo;
    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @GetMapping("/lock")
    @ConfigAnonymous
    public RestfulResult<?> lock(@RequestParam("key") String key) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                LogUtil.info(getClass(), "Running serial number：{}", finalI);
                redisDistributedLock.lock(key);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogUtil.info(getClass(), DateUtil.formatLong(new Date()));
                redisDistributedLock.unlock(key);
            }).start();
        }
        return RestfulResult.success();
    }

    @GetMapping("/set")
    @ConfigAnonymous
    public RestfulResult<?> set(@RequestParam String key) {
        String value = "Hello World!";
        redisCacheService.set(key, value, 60);
        return RestfulResult.success();
    }

    @GetMapping("/getKeys")
    @ConfigAnonymous
    public RestfulResult<?> getKeys(@RequestParam(required = false) String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = Strings.ASTERISK;
        } else {
            pattern += Strings.ASTERISK;
        }
        Set<String> res = redisCacheService.keys(pattern);
        return RestfulResult.success(res);
    }

    @GetMapping("/getValueByKey")
    @ConfigAnonymous
    public RestfulResult<?> getValueByKey(@RequestParam String key) {
        Object res = redisCacheService.get(key);
        if (res == null) {
            return RestfulResult.failure();
        }
        return RestfulResult.success(res);
    }

    @GetMapping("/deleteKey")
    @ConfigAnonymous
    public RestfulResult<?> deleteKey(@RequestParam String key) {
        redisCacheService.delete(key);
        return RestfulResult.success();
    }

    @GetMapping("/getCacheAll")
    @ConfigAnonymous
    public RestfulResult<?> getCacheAll() {
        List<SysUserDto> res = sysUserCacheRepo.findAll();
        return RestfulResult.success(res);
    }

    @GetMapping("/findCacheByKey")
    @ConfigAnonymous
    public RestfulResult<?> findCacheByKey(@RequestParam String key) {
        SysUserDto sysUserDto = sysUserCacheRepo.find(key);
        return RestfulResult.success(sysUserDto);
    }

    @GetMapping("/deleteCacheByKey")
    @ConfigAnonymous
    public RestfulResult<?> deleteCacheByKey(@RequestParam String key) {
        SysUserDto sysUserDto = sysUserCacheRepo.deleteByKey(key);
        return RestfulResult.success(sysUserDto);
    }

}
