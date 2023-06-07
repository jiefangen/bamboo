package org.panda.business.official.modules.system.service;

import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.entity.SysUser;

public interface SysUserService {

    SysUser getUserInfo(String username);

    SysUserDto getUserAndRoles(String username);

}
