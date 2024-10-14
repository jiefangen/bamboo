package org.panda.business.example.modules.components.mongo;


import org.panda.business.example.modules.system.model.dto.SysUserDto;
import org.panda.tech.data.mongo.repository.MongoUnityRepository;

public interface SysUserMongoRepox extends MongoUnityRepository<SysUserDto, String> {
}
