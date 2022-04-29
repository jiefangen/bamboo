package org.panda.modules.system.service;

import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.vo.MenuVO;

import java.math.BigInteger;
import java.util.List;

public interface MenuService {

    List<MenuPO> getMenus();

    List<MenuVO> getRoutes();

    List<BigInteger> getChildKeys(BigInteger menuId);
}
