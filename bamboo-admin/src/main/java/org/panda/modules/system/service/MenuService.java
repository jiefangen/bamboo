package org.panda.modules.system.service;

import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.vo.MenuVO;

import java.util.List;

public interface MenuService {

    List<MenuPO> getMenus();

    List<MenuVO> getRoutes();

}
