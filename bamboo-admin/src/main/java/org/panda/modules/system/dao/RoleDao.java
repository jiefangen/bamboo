package org.panda.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.dto.RoleDTO;
import org.panda.modules.system.domain.po.RolePO;
import org.panda.modules.system.domain.po.UserPO;

import javax.management.relation.Role;
import java.math.BigInteger;
import java.util.List;

public interface RoleDao {

    List<RolePO> findByUserId(@Param("userId") BigInteger userId);

    List<RolePO> findRoles();

    RolePO findByRoleName(@Param("roleName") String roleName);

    void insertRole(@Param("role") RolePO role);

    int updateRole(@Param("roleId") BigInteger roleId, @Param("role") RoleDTO role);

    int deleteRole(@Param("roleId") BigInteger roleId);

    Boolean delRoleVerify(@Param("roleId") BigInteger roleId);
}
