package org.panda.business.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.modules.system.service.entity.SysUserRole;

/**
 * <p>
 * 系统用户角色关系 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    SysUserDto getUserAndRoles(String username);
}
