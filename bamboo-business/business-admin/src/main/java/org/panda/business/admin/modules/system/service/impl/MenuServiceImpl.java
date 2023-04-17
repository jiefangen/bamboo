package org.panda.business.admin.modules.system.service.impl;

import org.panda.business.admin.common.exception.SystemException;
import org.panda.business.admin.modules.system.dao.MenuDao;
import org.panda.business.admin.modules.system.domain.po.MenuPO;
import org.panda.business.admin.modules.system.domain.vo.MenuVO;
import org.panda.business.admin.modules.system.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<MenuPO> getMenus() {
        return menuDao.findChildByParentId(BigInteger.ZERO);
    }

    @Override
    public List<MenuVO> getRoutes() {
        return menuDao.findRouteByParentId(BigInteger.ZERO);
    }

    private List<BigInteger> getParentOfChild(List<BigInteger> childKeys, BigInteger menuId) {
        BigInteger parentId = menuDao.findParentById(menuId);
        childKeys.add(menuId);
        if (parentId == null || parentId == BigInteger.ZERO) {
            return childKeys;
        } else {
            return getParentOfChild(childKeys, parentId);
        }
    }

    @Override
    public List<BigInteger> getChildKeys(BigInteger menuId) {
        return getParentOfChild(new LinkedList<>(), menuId);
    }

    @Override
    public void addMenu(MenuPO menu) {
        menuDao.insertMenu(menu);
    }

    @Override
    public int updateMenu(MenuPO menu) {
        return menuDao.updateMenu(menu);
    }

    @Override
    public int deleteMenu(BigInteger menuId) throws SystemException {
        // 校验该角色是否绑定的有用户或菜单权限资源
        if (menuDao.delMenuVerify(menuId)) {
            throw new SystemException("Delete the submenu or unbind the menu role first.");
        }
        return menuDao.deleteMenu(menuId);
    }
}
