package org.panda.business.admin.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.system.service.dto.SysRoleDto;
import org.panda.business.admin.modules.system.service.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统角色 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-06
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findRolesByUserId(@Param("userId") Integer userId);

    List<SysRole> findByUserId(@Param("userId") Integer userId);

    List<SysRole> findRoles();

    SysRole findByRoleName(@Param("roleName") String roleName);

    int updateRole(@Param("roleId") Integer roleId, @Param("role") SysRoleDto role);

    int deleteRole(@Param("roleId") Integer roleId);

    Boolean delRoleVerify(@Param("roleId") Integer roleId);

}
