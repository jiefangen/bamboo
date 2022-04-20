package org.panda.modules.system.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.panda.common.constant.enumeration.RoleType;
import org.panda.common.domain.ResultConstant;
import org.panda.common.exception.SystemException;
import org.panda.modules.system.dao.MenuDao;
import org.panda.modules.system.dao.RoleDao;
import org.panda.modules.system.domain.dto.RoleDTO;
import org.panda.modules.system.domain.dto.UserDTO;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.domain.vo.MenuVO;
import org.panda.modules.system.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
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

    private BigInteger getParentOfChild(BigInteger menuId) {
        BigInteger parentId = menuDao.findParentById(menuId);
        if (parentId == BigInteger.ZERO) {
            return menuId;
        } else {
            return getParentOfChild(parentId);
        }
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
    public int deleteRole(BigInteger roleId) throws SystemException {
        // TODO 校验该角色是否绑定的有用户
//        UserDTO useDto = this.getUserAndRoles(username);
//        List<RolePO> roles = useDto.getRoles();
//        if (CollectionUtils.isNotEmpty(roles)) {
//            throw new SystemException("Please unbind this user's role first.");
//        }

        return roleDao.deleteRole(roleId);
    }
}
