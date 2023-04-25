package org.panda.business.admin.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.business.admin.modules.system.model.dto.RoleDTO;
import org.panda.business.admin.modules.system.model.po.RolePO;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface RoleDao {

    List<RolePO> findByUserId(@Param("userId") BigInteger userId);

    List<RolePO> findRoles();

    RolePO findByRoleName(@Param("roleName") String roleName);

    void insertRole(@Param("role") RolePO role);

    int updateRole(@Param("roleId") BigInteger roleId, @Param("role") RoleDTO role);

    int deleteRole(@Param("roleId") BigInteger roleId);

    Boolean delRoleVerify(@Param("roleId") BigInteger roleId);
}
