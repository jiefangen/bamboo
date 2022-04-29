package org.panda.modules.system.service.impl;

import org.panda.modules.system.dao.MenuDao;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.domain.vo.MenuVO;
import org.panda.modules.system.service.MenuService;
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
}
