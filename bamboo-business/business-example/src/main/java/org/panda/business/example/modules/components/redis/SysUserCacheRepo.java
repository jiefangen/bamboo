package org.panda.business.example.modules.components.redis;


import org.panda.business.example.modules.system.model.dto.SysUserDto;
import org.panda.tech.data.redis.support.RedisValueUnityCacheRepoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserCacheRepo extends RedisValueUnityCacheRepoSupport<SysUserDto, String> {

    @Override
    public void deleteById(String id) {
        SysUserDto sysUserDto = deleteByKey(id);
        // do something
    }
}
