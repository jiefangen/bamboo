package org.panda.service.auth.repository;

import org.apache.ibatis.annotations.Param;
import org.panda.service.auth.model.entity.AuthPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * <p>
 * 应用资源权限 Mapper 接口
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
public interface AuthPermissionMapper extends BaseMapper<AuthPermission> {

    Set<String> selectPermissionsByRoleCodes(@Param("roleCodes") Set<String> roleCodes);

    Set<String> selectAnonymousPermissions(@Param("anonymousScope")String anonymousScope,
                                           @Param("anonymousDesc")String anonymousDesc);
}
