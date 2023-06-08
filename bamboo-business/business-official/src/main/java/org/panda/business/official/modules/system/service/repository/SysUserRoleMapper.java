package org.panda.business.official.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.official.modules.system.service.dto.SysUserDto;
import org.panda.business.official.modules.system.service.entity.SysUser;
import org.panda.business.official.modules.system.service.entity.SysUserRole;

/**
 * <p>
 * 系统用户角色关系 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 通过用户属性查询用户以及关联角色集
     *
     * @param user 用户属性
     * @return 用户以及关联角色集
     */
    SysUserDto findUserAndRoles(@Param("user") SysUser user);
}
