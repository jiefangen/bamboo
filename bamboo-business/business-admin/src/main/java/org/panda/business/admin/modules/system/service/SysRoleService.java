package org.panda.business.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.modules.system.service.dto.SysRoleDto;
import org.panda.business.admin.modules.system.service.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRoleDto> getRoles();

    String addRole(SysRole role);

    void updateRoleMenu(Integer roleId, SysRoleDto roleDto);

    int deleteRole(Integer roleId) throws BusinessException;
}
