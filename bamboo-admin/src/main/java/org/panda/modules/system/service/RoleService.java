package org.panda.modules.system.service;

import org.panda.common.exception.SystemException;
import org.panda.modules.system.domain.po.RolePO;

import java.math.BigInteger;
import java.util.List;

public interface RoleService {

    List<RolePO> getRoles();

    String addRole(RolePO role);

    int deleteRole(BigInteger roleId) throws SystemException;
}
