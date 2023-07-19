package org.panda.business.admin.modules.system.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.system.service.dto.SysUserDto;
import org.panda.business.admin.modules.system.service.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-07
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户属性查询用户以及关联角色集
     *
     * @param user 用户属性
     * @return 用户以及关联角色集
     */
    SysUserDto findUserAndRoles(@Param("user") SysUser user);

    void updateUserRole(@Param("userId") Integer userId, @Param("roleCodes") Set<String> roleCodes);
}
