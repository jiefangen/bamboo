package org.panda.modules.system.service.impl;

import org.panda.common.constant.enumeration.RoleType;
import org.panda.common.domain.ResultConstant;
import org.panda.common.exception.SystemException;
import org.panda.modules.system.dao.RoleDao;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<RolePO> getRoles() {
        return roleDao.findRoles();
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