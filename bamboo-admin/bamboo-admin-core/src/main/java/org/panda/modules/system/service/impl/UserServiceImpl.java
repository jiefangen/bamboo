package org.panda.modules.system.service.impl;

import com.github.pagehelper.Page;
import org.panda.modules.system.dao.RoleDao;
import org.panda.modules.system.dao.UserDao;
import org.panda.modules.system.domain.dto.UserDTO;
import org.panda.modules.system.domain.po.UserPO;
import org.panda.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jvfagan
 * @since JDK 1.8  2020/5/5
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Page<UserPO> getUsers(String keyword) {

        return userDao.findUsers(keyword);
    }

    @Override
    public UserDTO getUserAndRoles(String username) {
        UserPO userPO = userDao.findByUsername(username);
        UserDTO userDto = new UserDTO(userPO);
        userDto.setRoles(roleDao.findByUserId(userPO.getId()));
        return userDto;
    }
}
