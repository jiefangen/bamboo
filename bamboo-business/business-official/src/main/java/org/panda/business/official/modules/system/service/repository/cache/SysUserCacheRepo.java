package org.panda.business.official.modules.system.service.repository.cache;


import org.panda.business.official.modules.system.service.dto.SysUserDto;
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
