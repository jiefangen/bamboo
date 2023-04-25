package org.panda.business.admin.modules.system.service;

import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.model.po.MenuPO;
import org.panda.business.admin.modules.system.model.vo.MenuVO;

import java.math.BigInteger;
import java.util.List;

public interface MenuService {

    List<MenuPO> getMenus();

    List<MenuVO> getRoutes();

    List<BigInteger> getChildKeys(BigInteger menuId);

    void addMenu(MenuPO menu);

    int updateMenu(MenuPO menu);

    int deleteMenu(BigInteger menuId) throws SystemException;
}
