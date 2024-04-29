package org.panda.service.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.service.auth.model.entity.AuthPermission;
import org.panda.service.auth.model.param.GetPermissionParam;
import org.panda.service.auth.model.vo.PermissionInfoVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 应用资源权限 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
public interface AuthPermissionService extends IService<AuthPermission> {

    Set<String> getPermissions(Set<String> roleCodes);

    Set<String> getAnonymousPermission(String anonymousScope, String anonymousDesc);

    List<PermissionInfoVO> getPermissionInfo(GetPermissionParam permissionParam);
}
