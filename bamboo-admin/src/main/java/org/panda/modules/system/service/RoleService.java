package org.panda.modules.system.service;

import org.panda.modules.system.domain.po.RolePO;

import java.util.List;

public interface RoleService {

    List<RolePO> getRoles();

    String addRole(RolePO role);
}
