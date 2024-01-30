package org.panda.business.official.modules.system.service.repository.mongo;


import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.tech.data.mongo.repository.MongoUnityRepository;

public interface SysUserMongoRepox extends MongoUnityRepository<SysUserDto, String> {
}
