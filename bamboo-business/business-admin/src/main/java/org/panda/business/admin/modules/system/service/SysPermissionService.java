package org.panda.business.admin.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.panda.business.admin.modules.system.api.param.PermissionQueryParam;
import org.panda.business.admin.modules.system.service.entity.SysPermission;
import org.panda.tech.data.model.query.QueryResult;

import java.util.List;

/**
 * <p>
 * 系统权限 服务类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-25
 */
public interface SysPermissionService extends IService<SysPermission> {

    List<SysPermission> getPermissions(Integer roleId);

    QueryResult<SysPermission> getPermissionsByPage(PermissionQueryParam queryParam);
}
