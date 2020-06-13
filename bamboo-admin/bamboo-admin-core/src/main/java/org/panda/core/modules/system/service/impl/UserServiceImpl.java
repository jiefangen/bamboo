package org.panda.core.modules.system.service.impl;

import com.github.pagehelper.Page;
import org.panda.common.domain.ResultConstant;
import org.panda.core.modules.system.dao.RoleDao;
import org.panda.core.modules.system.dao.UserDao;
import org.panda.core.modules.system.domain.dto.UserDTO;
import org.panda.core.modules.system.domain.po.UserPO;
import org.panda.core.modules.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Page<UserPO> getUsers(String keyword) {
        return userDao.findUsers(keyword);
    }

    @Override
    public UserPO getUserInfo(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public UserDTO getUserAndRoles(String username) {
        UserPO userPO = userDao.findByUsername(username);
        UserDTO userDto = new UserDTO(userPO);
        userDto.setRoles(roleDao.findByUserId(userPO.getId()));
        return userDto;
    }

    @Override
    public String addUser(UserPO user) {
        // 校验username不能重复
        String username = user.getUsername();
        UserPO userPO = userDao.findByUsername(username);
        if (userPO != null) {
            String msg = "The username is already taken!";
            LOGGER.error(msg);
            return msg;
        }
        userDao.insertUser(user);
        return ResultConstant.DEFAULT_SUCESS_MSG;
    }

    @Override
    public int updateUser(UserPO user) {
        // 更新时间
        user.setUpdateTime(new Date());
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(String id) {
        return userDao.deleteUser(id);
    }
}
