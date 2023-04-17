package org.panda.business.admin.modules.system.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.panda.business.admin.common.constant.enumeration.RoleType;
import org.panda.business.admin.common.domain.ResultConstant;
import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.dao.MenuDao;
import org.panda.business.admin.modules.system.dao.RoleDao;
import org.panda.business.admin.modules.system.domain.dto.RoleDTO;
import org.panda.business.admin.modules.system.domain.po.RolePO;
import org.panda.business.admin.modules.system.domain.vo.MenuVO;
import org.panda.business.admin.modules.system.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<RoleDTO> getRoles() {
        List<RolePO> roles = roleDao.findRoles();
        List<RoleDTO> roleList = new ArrayList<>();
        roles.forEach(role -> {
            // 添加该角色对应的菜单权限
            RoleDTO roleDTO = new RoleDTO(role);
            String idAndRoleId = "0,".concat(String.valueOf(role.getId()));
            List<MenuVO> roleRoutes = menuDao.findRouteByRoleId(idAndRoleId);
            roleDTO.setRoutes(roleRoutes);
            roleList.add(roleDTO);
        });
        return roleList;
    }

    @Override
    public String addRole(RolePO role) {
        // 校验username不能重复
        String roleName = role.getRoleName();
        RolePO userPO = roleDao.findByRoleName(roleName);
        if (userPO != null) {
            String msg = "The roleName is already taken!";
            LOGGER.error(msg);
            return msg;
        }
        // 默认roleType
        role.setRoleCode(RoleType.CUSTOMER.name());
        roleDao.insertRole(role);
        return ResultConstant.DEFAULT_SUCCESS_MSG;
    }

    @Override
    public void updateRole(BigInteger roleId, RoleDTO roleDTO) {
        // 更新角色信息
        roleDTO.setUpdateTime(new Date());
        roleDao.updateRole(roleId, roleDTO);
        // 更新角色权限
        List<BigInteger> routeIds = new ArrayList<>();
        getRoleRoutes(roleDTO.getRoutes(), routeIds);
        menuDao.updateRoleRoutes(roleId, routeIds);
    }

    private void getRoleRoutes(List<MenuVO> routes, List<BigInteger> routeIds) {
        if (CollectionUtils.isNotEmpty(routes)) {
            routes.forEach(route -> {
                routeIds.add(route.getId());
                getRoleRoutes(route.getChildren(), routeIds);
            });
        }
        return;
    }

    @Override
    public int deleteRole(BigInteger roleId) throws SystemException {
        // 校验该角色是否绑定的有用户或菜单权限资源
        if (roleDao.delRoleVerify(roleId)) {
            throw new SystemException("Please unbind the user or menu resource of this role first.");
        }
        return roleDao.deleteRole(roleId);
    }
}
