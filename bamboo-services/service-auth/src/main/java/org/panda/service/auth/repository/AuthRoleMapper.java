package org.panda.service.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.panda.service.auth.model.entity.AuthRole;

import java.util.List;

/**
 * <p>
 * 应用认证角色 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
public interface AuthRoleMapper extends BaseMapper<AuthRole> {

    List<AuthRole> getRoleByPermissionId(@Param("permissionId") Integer permissionId);

}
