package org.panda.business.admin.modules.system.service;

import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.domain.dto.RoleDTO;
import org.panda.business.admin.modules.system.domain.po.RolePO;

import java.math.BigInteger;
import java.util.List;

public interface RoleService {

    List<RoleDTO> getRoles();

    String addRole(RolePO role);

    void updateRole(BigInteger roleId, RoleDTO roleDTO);

    int deleteRole(BigInteger roleId) throws SystemException;
}
