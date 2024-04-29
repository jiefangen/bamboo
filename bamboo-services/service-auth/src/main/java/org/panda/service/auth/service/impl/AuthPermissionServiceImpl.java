package org.panda.service.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.jackson.JsonUtil;
import org.panda.service.auth.model.entity.AuthPermission;
import org.panda.service.auth.model.entity.AuthRole;
import org.panda.service.auth.model.param.GetPermissionParam;
import org.panda.service.auth.model.vo.PermissionInfoVO;
import org.panda.service.auth.repository.AuthPermissionMapper;
import org.panda.service.auth.repository.AuthRoleMapper;
import org.panda.service.auth.service.AuthPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 应用资源权限 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-10-25
 */
@Service
public class AuthPermissionServiceImpl extends ServiceImpl<AuthPermissionMapper, AuthPermission> implements AuthPermissionService {

    @Resource
    private AuthRoleMapper authRoleMapper;

    @Override
    public Set<String> getPermissions(Set<String> roleCodes) {
        return this.baseMapper.selectPermissionsByRoleCodes(roleCodes);
    }

    @Override
    public Set<String> getAnonymousPermission(String anonymousScope, String anonymousDesc) {
        return this.baseMapper.selectAnonymousPermissions(anonymousScope, anonymousDesc);
    }

    @Override
    public List<PermissionInfoVO> getPermissionInfo(GetPermissionParam permissionParam) {
        LambdaQueryWrapper<AuthPermission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AuthPermission::getResourcesId, permissionParam.getResourcesId())
                    .eq(AuthPermission::getSource, permissionParam.getAppName());
        List<AuthPermission> authPermissions = this.list(queryWrapper);
        List<PermissionInfoVO> permissionInfoVOS = new ArrayList<>();
        if (!authPermissions.isEmpty()) {
            authPermissions.forEach(authPermission -> {
                PermissionInfoVO permissionInfoVO = JsonUtil.json2Bean(JsonUtil.toJson(authPermission), PermissionInfoVO.class);
                List<AuthRole> authRoles = authRoleMapper.getRoleByPermissionId(authPermission.getId());
                if (!authRoles.isEmpty()) {
                    StringBuilder roleScopeSb = new StringBuilder();
                    authRoles.forEach(authRole -> {
                        roleScopeSb.append(authRole.getRoleCode()).append(Strings.VERTICAL_BAR);
                    });
                    permissionInfoVO.setRoleScope(roleScopeSb.toString());
                }
                permissionInfoVOS.add(permissionInfoVO);
            });
        }
        return permissionInfoVOS;
    }
}
