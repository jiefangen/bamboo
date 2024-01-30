package org.panda.business.official.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.entity.SysUserRole;

/**
 * <p>
 * 系统用户角色关系 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    SysUserDto getUserAndRoles(String username);

}
