package org.panda.modules.system.dao;

import org.apache.ibatis.annotations.Param;
import org.panda.modules.system.domain.po.MenuPO;

import java.math.BigInteger;
import java.util.List;

public interface MenuDao {

    List<MenuPO> findChildByParentId(@Param("parentId") BigInteger parentId);

}
