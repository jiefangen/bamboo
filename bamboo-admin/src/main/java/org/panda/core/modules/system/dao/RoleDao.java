package org.panda.core.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.core.modules.system.domain.po.RolePO;

import java.math.BigInteger;
import java.util.List;

public interface RoleDao {

    List<RolePO> findByUserId(@Param("userId") BigInteger userId);
}
