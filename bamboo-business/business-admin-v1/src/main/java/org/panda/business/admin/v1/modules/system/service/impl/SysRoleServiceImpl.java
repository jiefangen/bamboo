package org.panda.business.admin.v1.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.exception.business.BusinessException;
import org.panda.business.admin.v1.common.constant.enums.RoleCode;
import org.panda.business.admin.v1.modules.system.api.vo.MenuVO;
import org.panda.business.admin.v1.modules.system.service.SysRoleService;
import org.panda.business.admin.v1.modules.system.service.dto.SysRoleDto;
import org.panda.business.admin.v1.modules.system.service.entity.SysRole;
import org.panda.business.admin.v1.modules.system.service.repository.SysMenuMapper;
import org.panda.business.admin.v1.modules.system.service.repository.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author bamboo-code-generator
 * @since 2023-06-24
 */
@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysMenuMapper menuDao;

    @Override
    public List<SysRoleDto> getRoles() {
        List<SysRole> roles = this.baseMapper.findRoles();
        List<SysRoleDto> roleList = new ArrayList<>();
        roles.forEach(role -> {
            // 添加该角色对应的菜单权限
            SysRoleDto roleDTO = new SysRoleDto(role);
            String idAndRoleId = "0,".concat(String.valueOf(role.getId()));
            List<MenuVO> roleRoutes = menuDao.findRouteByRoleId(idAndRoleId);
            roleDTO.setRoutes(roleRoutes);
            roleList.add(roleDTO);
        });
        return roleList;
    }

    @Override
    public String addRole(SysRole role) {
        String roleName = role.getRoleName();
        SysRole sysRole = this.baseMapper.findByRoleName(roleName);
        if (sysRole != null) {
            return "The roleName is already taken!";
        }
        // 默认roleType
        role.setRoleCode(RoleCode.CUSTOMER.name());
        this.save(role);
        return Commons.RESULT_SUCCESS;
    }

    @Override
    public void updateRole(Integer roleId, SysRoleDto roleDTO) {
        // 更新角色信息
        this.baseMapper.updateRole(roleId, roleDTO);
        // 更新角色权限
        List<Integer> routeIds = new ArrayList<>();
        getRoleRoutes(roleDTO.getRoutes(), routeIds);
        menuDao.updateRoleRoutes(roleId, routeIds);
    }

    private void getRoleRoutes(List<MenuVO> routes, List<Integer> routeIds) {
        if (CollectionUtils.isNotEmpty(routes)) {
            routes.forEach(route -> {
                routeIds.add(route.getId());
                getRoleRoutes(route.getChildren(), routeIds);
            });
        }
    }

    @Override
    public int deleteRole(Integer roleId) throws BusinessException {
        // 校验该角色是否绑定的有用户或菜单权限资源
        if (this.baseMapper.delRoleVerify(roleId)) {
            throw new BusinessException("Please unbind the user or menu resource of this role first.");
        }
        return this.baseMapper.deleteRole(roleId);
    }
}
