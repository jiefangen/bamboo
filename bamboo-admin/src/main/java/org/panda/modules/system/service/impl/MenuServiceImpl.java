package org.panda.modules.system.service.impl;

import org.panda.modules.system.dao.MenuDao;
import org.panda.modules.system.domain.po.MenuPO;
import org.panda.modules.system.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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
}
