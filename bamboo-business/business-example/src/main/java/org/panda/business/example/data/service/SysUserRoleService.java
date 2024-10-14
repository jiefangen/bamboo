package org.panda.business.example.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.example.data.entity.SysUserRole;
import org.panda.business.example.modules.system.model.dto.SysUserDto;

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
